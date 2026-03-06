# 詳細設計書 - カスタムAVFランチャー

## 1. アプリケーション構成

### 1.1 画面構成と遷移
1.  **VM一覧画面 (LauncherActivity)**: 
    - 保存済みのVM設定をカード形式で表示。
    - 各カードには「起動」「停止」「設定変更」「削除」ボタンを配置。
    - 「新規作成 (+)」ボタンで作成画面へ遷移。
2.  **VM作成・編集画面 (VmConfigActivity)**:
    - カーネルおよびディスクイメージの選択（特定のフォルダ `/storage/emulated/0/AVF/images/` 等からの選択）。
    - カスタムパラメータ設定（起動引数や追加のconfig JSON等）。
    - CPU個数（1〜4）、メモリ量（512MB〜4GB）のスライダー。
    - ディスクサイズの設定。
    - 保存ボタンで `VmDatabase` に記録。
3.  **VM実行画面 (VmConsoleActivity)**:
    - 仮想ディスプレイを表示する `TextureView` または `SurfaceView`。
    - Android 16の `VirtualMachineDisplayConfig` を使用。
    - ソフトキーボード/マウスエミュレーションのオーバーレイ。
4.  **Configエディタ画面 (VmJsonEditorActivity)**:
    - `vm_config.json` の内容を直接テキスト編集。
    - JSONバリデーション機能。
    - 保存ボタンで `VMConfig` エンティティの `customParams` または専用フィールドに反映。

### 1.2 データモデル
- **VMConfig (Entity)**:
    - `id`: UUID
    - `name`: string
    - `kernelPath`: string (絶対パス)
    - `diskImagePath`: string (絶対パス)
    - `configJson`: string (vm_config.jsonの生テキスト)
    - `customParams`: string (起動引数等の追加パラメータ)
    - `cpuCount`: int
    - `memoryMb`: int
    - `diskSizeGb`: int
    - `useGpu`: boolean

- **VmDatabase (Room)**: 
    - 上記 `VMConfig` を永続化。

## 2. AVF 統合設計

### 2.1 VirtualizationService の利用
- `VirtualizationService` へのバインドを行い、`VirtualMachineManager` を取得する。
- 権限: `android.permission.USE_CUSTOM_VIRTUAL_MACHINE` を `AndroidManifest.xml` に定義。

### 2.2 VMの構成作成 (Android 16 準拠)
```kotlin
val config = VirtualMachineConfig.Builder(context)
    .setPayloadBinaryName("kernel") // またはカスタムイメージ
    .setPayloadConfigPath("config.json")
    .setMemoryBytes(memoryMb * 1024L * 1024L)
    .setCpuCount(cpuCount)
    .setVirtualMachineCustomImageConfig(
        VirtualMachineCustomImageConfig.Builder()
            .setOsImageFileDescriptor(fileDescriptor)
            .build()
    )
    .setVirtualMachineDisplayConfig(
        VirtualMachineDisplayConfig.Builder()
            .setGpuConfig(VirtioGpuConfig.Builder().build())
            .build()
    )
    .build()
```
*(注: Android 16の実際のAPI名は開発プレビューに基づき調整が必要)*

### 2.3 ライフサイクル管理
- `VirtualMachine` オブジェクトを `ViewModel` または `Service` で保持。
- ステータス変更 (STARTING, RUNNING, STOPPED) を `LiveData` でUIに通知。

## 3. ストレージとネットワーク

### 3.1 ファイルアクセス
- `ActivityResultLauncher` で `ACTION_OPEN_DOCUMENT` を使用し、ユーザーが選択したイメージファイルをアプリの内部ストレージにコピーするか、FileDescriptorを保持する。

### 3.2 ネットワーク
- 基本的に `VirtualMachineConfig.Builder.setNetworkConfig` でデフォルトのNATモードを利用。

## 4. UI/UX デザイン方針
- **テーマ**: モダンなダークモード。
- **フィードバック**: VMの起動ログ（コンソール出力）を別タブまたはオーバーレイでリアルタイム表示。
- **エラー処理**: ハードウェア仮想化が無効な場合や、メモリ不足時の適切なエラーメッセージ表示。
