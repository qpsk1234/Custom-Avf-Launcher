# 要件定義書 - カスタムAVFランチャー

## 1. プロジェクト概要
Android Virtualization Framework (AVF) を活用し、Android 16以降のデバイス上でカスタムOS（主にLinux配布版）を仮想マシン(VM)として動作させるためのランチャーアプリを作成する。

## 2. ターゲットプラットフォーム
- **対象OS**: Android 16 (API Level 36想定) 以降
- **ハードウェア要件**: AVFをサポートし、pKVM (Protected KVM) が有効化され、仮想化拡張機能およびGPU加速が利用可能なデバイス

## 3. 主要機能 (要件)

### 3.1 OSイメージ管理
- 外部ストレージからのOSイメージ（ISO, RAW, QCOW2等）のインポート
- インポート済みイメージの一覧表示と削除
- イメージ形式の自動判別（Microdroid, Generic Linux等）

### 3.2 VMコンフィグレーション
- 各VMに対する以下のリソース割り当て設定：
    - CPUコア数
    - メモリ容量 (RAM)
    - ストレージサイズ（仮想ディスク）
- グラフィックス設定（GPU加速の有効/無効）
- ネットワーク設定（ブリッジ/NAT等、AVFの仕様に準ずる）

### 3.3 VMライフサイクル管理
- VMの起動 (Start)
- VMの停止 (Shutdown/Power-off)
- VMの状態表示 (Running, Stopped, Starting, etc.)

### 3.4 ユーザーインターフェース (UI)
- VM一覧表示画面
- VM作成・設定編集画面
- VM実行画面（仮想ディスプレイ/コンソール出力の表示）
- Android 16の新しい「Terminal」連携（可能であれば）

## 4. 非機能要件
- **セキュリティ**: AVFの隔離環境を利用し、ホストOS（Android）の安全性を確保する。
- **操作性**: 直感的なUIで、高度な仮想化設定を簡略化して提供する。
- **パフォーマンス**: Android 16で導入されるVirtio-GPU等を活用し、円滑なGUI操作を実現する。

## 5. 技術スタック
- **開発言語**: Kotlin
- **ビルドツール**: Gradle
- **主要API**:
    - `android.system.virtualmachine` (VirtualizationService)
    - Android 16 仮想化関連の新機能 (Virtio-GPU, Linux VMサポート強化)
