# customnpcs-char-gen

Minecraft の mod CustomNPCs のクローナー json を生成します。
このソフトウェアは、MITライセンスのもとで公開されています。詳細は LICENSE を見てください。

## 概要

スキンを大量に取得し、CustomNPCs で別々のスキンを持ったたくさんのキャラを作ろうとするとめんどくさいです。
クローナーで作ったキャラのスキンだけを大量に入れ替えたい時にこれをつかうと、さくっと作ることができます。

## 必要ライブラリ/ソフトウェア

* JRE1.8 以上
  * http://www.oracle.com/technetwork/jp/java/javase/downloads/index.html
* Minecraft 1.10.2
  * https://minecraft.net/
* CustomNPCs 1.10.2
  * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1278956-custom-npcs
  * http://www.kodevelopment.nl/minecraft/customnpcs

## インストール

zip を解凍するだけ

## アンインストール

解凍したフォルダを削除するだけ

## 使用方法

### 事前準備(スキンファイル配置)

1. スキンを取得し、[マインクラフトインストールしたフォルダ]/customnpcs/assets/customnpcs/textures 内に配置します。
   ※上記フォルダ内にさらにフォルダをおいてもOK
1. 古いスキン(64x32)のファイル名の最後に"_32"とつけてください。
   例) test.png -> test_32.png

### 事前準備(ベースとなるキャラクターJsonファイル作成)

1. CustomNPCs で NPC を作成し、クローナーでクライアントに保存してください。
   もし作るのがめんどくさかったら、sampleCharacterJson内にあるファイルを、[マインクラフトをインストールしたフォルダ]/customnpcs/assets/clones/1/ に配置してください。

### 実行

1. /bin/application.properties を開き、下記項目を修正してください。
  * minecraftInstallDir  
    マインクラフトをインストールしたフォルダをスラッシュ"/"区切りで記述してください。  
    例) minecraftInstallDir=C:/Users/k/AppData/Roaming/.minecraft  
    例) minecraftInstallDir=C:/k/app/minecraft_v1.11.2_forge  
  * minecraftBaseJsons
    キャラ作成時のベースとなるキャラクターの json ファイル名をカンマ","区切りで入力してください。
    マインクラフトをインストールしたフォルダの /customnpcs/clones/[番号]/ 内にあるものを検索して使用します。
    例) minecraftBaseJsons=sample1.json,sample2.json,sample3.json,sample4.json
2. /bin/customnpcs-char-gen.bat を実行してください。
3. /bin/export フォルダ内に json ファイルができますので、[マインクラフトをインストールしたフォルダ]/customnpcs/assets/clones/[番号]/ 内に配置してください。
4. あとはマインクラフトを起動して、CustomNPCsでいつものようにクローナーでキャラを呼び出す手順でNPCを追加できます。