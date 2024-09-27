import Phaser from 'phaser';
import { useEffect } from 'react';
import { createGameMap } from './GameMap';
import { createPlayerMovement } from './PlayerMovement';

const PhaserGame = () => {
  useEffect(() => {
    const config: Phaser.Types.Core.GameConfig = {
      type: Phaser.AUTO,
      width: 1000,
      height: 600,
      physics: {
        default: 'arcade',
        arcade: {
          gravity: { x: 0, y: 0 },
          fps: 60,
          tileBias: 16,
          debug: true,
        },
      },
      scene: {
        preload,
        create,
        update,
      },
      render: {
        antialias: false,
        pixelArt: true,
        roundPixels: true,
      },
    };

    const game = new Phaser.Game(config);

    return () => {
      game.destroy(true);
    };
  }, []);

  let player: Phaser.Physics.Arcade.Sprite;
  let cursors: Phaser.Types.Input.Keyboard.CursorKeys;
  let spaceBar: Phaser.Input.Keyboard.Key;
  let mapLayers: any;

  function preload(this: Phaser.Scene) {
    this.load.tilemapTiledJSON('map', '../src/assets/background/testmap.json');
    this.load.image(
      'tileset',
      '../src/assets/background/spr_tileset_sunnysideworld_16px.png',
    );

    // 동물들 텍스처 로드
    this.load.image(
      'bird_image',
      '../src/assets/background/animals/spr_deco_bird_01_strip4.png',
    );
    this.load.image(
      'blinking_image',
      '../src/assets/background/animals/spr_deco_blinking_strip12.png',
    );
    this.load.image(
      'chicken_image',
      '../src/assets/background/animals/spr_deco_chicken_01_strip4.png',
    );
    this.load.image(
      'cow_image',
      '../src/assets/background/animals/spr_deco_cow_strip4.png',
    );
    this.load.image(
      'duck_image',
      '../src/assets/background/animals/spr_deco_duck_01_strip4.png',
    );
    this.load.image(
      'pig_image',
      '../src/assets/background/animals/spr_deco_pig_01_strip4.png',
    );
    this.load.image(
      'sheep_image',
      '../src/assets/background/animals/spr_deco_sheep_01_strip4.png',
    );

    // 식물, 풍차 등
    this.load.image(
      'mushroom_blue_01_image',
      '../src/assets/background/others/spr_deco_mushroom_blue_01_strip4.png',
    );
    this.load.image(
      'mushroom_blue_02_image',
      '../src/assets/background/others/spr_deco_mushroom_blue_02_strip4.png',
    );
    this.load.image(
      'mushroom_blue_03_image',
      '../src/assets/background/others/spr_deco_mushroom_blue_03_strip4.png',
    );
    this.load.image(
      'mushroom_red_01_image',
      '../src/assets/background/others/spr_deco_mushroom_red_01_strip4.png',
    );
    this.load.image(
      'windmill_image',
      '../src/assets/background/others/spr_deco_windmill_withshadow_strip9.png',
    );

    // 연기
    this.load.image(
      'chimneysmoke_01_01_image',
      '../src/assets/background/others/chimneysmoke_01_strip30_01.png',
    );
    this.load.image(
      'chimneysmoke_04_01_image',
      '../src/assets/background/others/chimneysmoke_04_strip30_01.png',
    );
    this.load.image(
      'chimneysmoke_05_01_image',
      '../src/assets/background/others/chimneysmoke_05_strip30_01.png',
    );

    // 플레이어 텍스처 로드
    this.load.spritesheet(
      'player',
      '../src/assets/background/characters/ninja_skeleton.png',
      {
        frameWidth: 16,
        frameHeight: 19,
      },
    );

    this.load.image('collides', '../src/assets/background/collides.png');
  }

  function create(this: Phaser.Scene) {
    mapLayers = createGameMap(this);
    //mapLayer가 정의되지 않았면 빈 객체로 처리해준다.
    const {
      backgroundLayer,
      groundLayer,
      animals_topLayer,
      animals_bottomLayer,
      collidesLayer,
      shadow_seaLayer,
      river_lakeLayer,
      dropfruitsLayer,
      fencesLayer,
      plantLayer,
      vegetableLayer,
      shadow_topLayer,
      house_bottom2Layer,
      house_bottomLayer,
      house_topLayer,
      shadow_bottomLayer,
    } = mapLayers ?? {};

    void backgroundLayer;
    void groundLayer;
    void animals_topLayer;
    void animals_bottomLayer;
    void collidesLayer;
    void dropfruitsLayer;
    void fencesLayer;
    void plantLayer;
    void vegetableLayer;
    void shadow_bottomLayer;
    void shadow_seaLayer;
    void shadow_topLayer;
    void house_bottom2Layer;
    void house_bottomLayer;
    void house_topLayer;
    void river_lakeLayer;

    player = this.physics.add.sprite(2240, 1380, 'player');
    player.setCollideWorldBounds(true);
    player.setScale(1);

    // 충돌 설정
    //this.physics.add.collider(player, collidesLayer as Phaser.Tilemaps.TilemapLayer);

    this.physics.world.setBounds(0, 0, 4480, 2560);
    this.cameras.main.setBounds(0, 0, 4480, 2560);
    this.cameras.main.startFollow(player, true, 0.5, 0.5);

    cursors = this.input.keyboard!.createCursorKeys();
    spaceBar = this.input.keyboard!.addKey(
      Phaser.Input.Keyboard.KeyCodes.SPACE,
    );

    this.anims.create({
      key: 'walk',
      frames: this.anims.generateFrameNumbers('player', { start: 0, end: 7 }),
      frameRate: 10,
      repeat: -1,
    });
  }

  function update(this: Phaser.Scene) {
    createPlayerMovement(this, player, cursors);
    //거래소 및 마을에서의 상호작용 기능
    if (Phaser.Input.Keyboard.JustDown(spaceBar)) {
      const {
        house1Layer,
        house2Layer,
        house3Layer,
        house4Layer,
        house5Layer,
        house6Layer,
        exchangeLayer,
      } = mapLayers;

      const playerTileX = Math.floor(player.x / 16);
      const playerTileY = Math.floor(player.y / 16);

      const house1Tile = house1Layer?.hasTileAt(playerTileX, playerTileY);
      const house2Tile = house2Layer?.hasTileAt(playerTileX, playerTileY);
      const house3Tile = house3Layer?.hasTileAt(playerTileX, playerTileY);
      const house4Tile = house4Layer?.hasTileAt(playerTileX, playerTileY);
      const house5Tile = house5Layer?.hasTileAt(playerTileX, playerTileY);
      const house6Tile = house6Layer?.hasTileAt(playerTileX, playerTileY);
      const exchangeTile = exchangeLayer?.hasTileAt(playerTileX, playerTileY);

      if (house1Tile) {
        console.log('1번집입니다.');
      } else if (house2Tile) {
        console.log('2번집입니다.');
      } else if (house3Tile) {
        console.log('3번집입니다.');
      } else if (house4Tile) {
        console.log('4번집입니다.');
      } else if (house5Tile) {
        console.log('5번집입니다.');
      } else if (house6Tile) {
        console.log('6번집입니다.');
      } else if (exchangeTile) {
        console.log('거래소입니다.');
      } else {
        console.log('상호작용이 불가능한 위치입니다.');
      }
    }
  }

  return <div id="phaser-game-container" />;
};

export default PhaserGame;
