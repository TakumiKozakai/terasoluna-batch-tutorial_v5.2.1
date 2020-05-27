20200501_terasoluna-batch-tutorial_v5.2.1

1.概要及び背景
　terasoluna-batch-FW_v5.2.1のチュートリアルを実施。
　業務利用のために学習。

2.実行環境
　OS		：MacOX High Sierra バージョン 10.13.6
　JDK		：openjdk-11.0.2
　IDE		：pleiades-2020-03-java-mac-jre_20200322
　Build Tool	：Apache Maven 3.6.3_1(HomeBrew導入)
　RDBMS		：PostgreSQL 12.2_1(HomeBrew導入)

3.実施内容
　9.4.バッチジョブの実装
　　9.4.1. データベースアクセスでデータ入出力を行うジョブ
　　9.4.2. ファイルアクセスでデータ入出力を行うジョブ
　　9.4.3. 入力データの妥当性検証を行うジョブ
　　9.4.4. ChunkListenerで例外ハンドリングを行うジョブ
　　9.4.5. try-catchで例外ハンドリングを行うジョブ

4.利用時の留意事項
　ローカルへのMavenリポジトリのダウンロードのために、
　setting.xml（Mavenディレクトリ配下）を記述する必要がある。
　私用PCであれば不要だが、業務PCであればプロキシの設定（記述）も必要。
　上記設定をしていない場合、
　プロジェクト作成コマンド、Mvn build（clean）コマンドでエラーになる。

*.参照
　https://terasoluna-batch.github.io/guideline/current/ja/single_index.html#Ch09