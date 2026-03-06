# Custom AVF Launcher for Android 16+

Android Virtualization Framework (AVF) を使用して、カスタムOS（Linux等）を仮想マシンとして起動・管理する Android 16+ 向けのランチャーアプリです。

## 概要

Android 16で強化されたAVFの機能を活用し、デバイス上で保護された仮想実行環境（pKVM）を構築します。OSイメージの読み込みから、`vm_config.json` の直接編集、グラフィカルコンソールでの操作まで、高度な仮想化機能を直感的なUIで提供します。

## 主要機能

- **Android 16 仮想化 API 対応**: 
    - `VirtualizationService` を利用した VM ライフサイクル管理
    - pKVM (Protected KVM) のサポート確認と保護された実行環境
    - Android 16+ の `VirtualMachineDisplayConfig` によるグラフィカル出力と GPU 加速 (Virtio-GPU)
- **柔軟なイメージ管理**: 
    - ローカルストレージ（`/storage/emulated/0/AVF/images/`）からのカーネルおよびディスクイメージの読み込み。
- **Config エディタ**: 
    - 仮想マシンの詳細設定ファイル `vm_config.json` をアプリ内で直接テキスト編集可能。
- **モダンな UI**: 
    - Jetpack Compose を使用した Material 3 デザインの UI。
- **データ永続化**: 
    - Room DB を使用して、作成した VM の設定を保存。

## 動作要件

- **OS**: Android 16 (API Level 36) 以降
- **ハードウェア**: AVF および pKVM が有効化されたデバイス（Google Pixel 6以降等）
- **権限**: `android.permission.USE_CUSTOM_VIRTUAL_MACHINE` (システム署名または開発者向け設定が必要)

## プロジェクト構成

- `SPEC/`: 要件定義、詳細設計ドキュメント
- `app/`: Android アプリケーションソースコード
    - `core/`: AVF 管理クラス、Room DB、データモデル
    - `ui/`: Compose 画面、ViewModel、ナビゲーション

## 開発と実行

1. Android デバイスの `/storage/emulated/0/AVF/images/` に `kernel` および `disk.img` を配置してください。
2. Android Studio でプロジェクトを開き、Android 16 端末/エミュレータにデプロイしてください。

## 詳細設計

開発の詳細な意図や構成については、[SPEC/detailed_design.md](SPEC/detailed_design.md) を参照してください。
