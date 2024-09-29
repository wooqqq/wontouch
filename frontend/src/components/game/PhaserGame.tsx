import Phaser from 'phaser';
import { useEffect, useState } from 'react';
import { createGameMap } from './GameMap';
import { createPlayerMovement } from './PlayerMovement';
import InteractionModal from './InteractionModal';

interface MapLayers {
  backgroundLayer: Phaser.Tilemaps.TilemapLayer | null;
  groundLayer: Phaser.Tilemaps.TilemapLayer | null;
  animals_topLayer: Phaser.Tilemaps.TilemapLayer | null;
  animals_bottomLayer: Phaser.Tilemaps.TilemapLayer | null;
  collidesLayer: Phaser.Tilemaps.TilemapLayer | null;
  shadow_seaLayer: Phaser.Tilemaps.TilemapLayer | null;
  river_lakeLayer: Phaser.Tilemaps.TilemapLayer | null;
  dropfruitsLayer: Phaser.Tilemaps.TilemapLayer | null;
  fencesLayer: Phaser.Tilemaps.TilemapLayer | null;
  plantLayer: Phaser.Tilemaps.TilemapLayer | null;
  vegetableLayer: Phaser.Tilemaps.TilemapLayer | null;
  shadow_topLayer: Phaser.Tilemaps.TilemapLayer | null;
  house_bottom2Layer: Phaser.Tilemaps.TilemapLayer | null;
  house_bottomLayer: Phaser.Tilemaps.TilemapLayer | null;
  house_topLayer: Phaser.Tilemaps.TilemapLayer | null;
  shadow_bottomLayer: Phaser.Tilemaps.TilemapLayer | null;

  house1Layer: Phaser.Tilemaps.TilemapLayer | null;
  house2Layer: Phaser.Tilemaps.TilemapLayer | null;
  house3Layer: Phaser.Tilemaps.TilemapLayer | null;
  house4Layer: Phaser.Tilemaps.TilemapLayer | null;
  house5Layer: Phaser.Tilemaps.TilemapLayer | null;
  house6Layer: Phaser.Tilemaps.TilemapLayer | null;
  exchangeLayer: Phaser.Tilemaps.TilemapLayer | null;
}

const PhaserGame = () => {
  const [houseNum, setHouseNum] = useState<number | null>(null);
  useEffect(() => {
    const config: Phaser.Types.Core.GameConfig = {
      type: Phaser.AUTO,
      width: window.innerWidth,
      height: window.innerHeight,
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
  let mapLayers: MapLayers | undefined | null;

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
    this.load.image(
      'timerBackground',
      '../src/assets/background/round/timerBackground.png',
    );
  }

  //생성
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
    this.cameras.main.setZoom(2, 2);

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

    const { width, height } = this.scale;

    //이미지
    const timerBackground = this.add.image(
      width / 2,
      height / 2,
      'timerBackground',
    );
    timerBackground.setDisplaySize(80, 30);
    timerBackground.setScrollFactor(0);
    //라운드 텍스트 생성
    const roundText = this.add.text(width / 2, height / 2, '1R', {
      fontFamily: 'DNFBitBitv2',
      color: '#FFEE00', // 텍스트 색상
      fontSize: '14px',
      stroke: '#000000', // 스토크 색상 (검정색)
      strokeThickness: 4, // 스토크 두께
    });

    // 타이머 텍스트 생성 (화면 중앙에 배치)
    const timeText = this.add.text(width / 2, height / 2, '5 : 00', {
      fontFamily: 'DNFBitBitv2',
      fontSize: '14px',
      color: '#ffffff', // 텍스트 색상
      stroke: '#000000', // 스토크 색상 (검정색)
      strokeThickness: 2, // 스토크 두께
    });

    //이미지 위치설정
    timerBackground.setOrigin(0.5, 5.9);
    timerBackground.setDepth(9);

    //라운드 위치 설정
    roundText.setOrigin(0.5, 8.3);
    roundText.setScrollFactor(0);
    roundText.setDepth(10);
    //타이머 위치설정
    timeText.setOrigin(0.5, 8.25);

    // 타이머 변수 초기화
    let timer = 300;

    // 타이머 텍스트를 화면에 고정
    timeText.setScrollFactor(0);
    timeText.setDepth(10); // 텍스트가 다른 객체 위에 표시되도록 설정

    // 타이머 이벤트 설정 (1초마다 업데이트)
    this.time.addEvent({
      delay: 1000, // 1초마다 실행
      callback: () => {
        if (timer !== 0) {
          timer--; // 1초마다 timer 값 감소
        }
        timeText.setText(
          `${Math.floor(timer / 60)} : ${timer % 60 < 10 ? '0' : ''}${timer % 60}`,
        ); // 타이머 값을 업데이트
      },
      loop: true, // 반복 실행
    });
  }

  //업데이트
  function update(this: Phaser.Scene) {
    createPlayerMovement(this, player, cursors, 16);
    //거래소 및 마을에서의 상호작용 기능
    if (mapLayers && Phaser.Input.Keyboard.JustDown(spaceBar)) {
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
        setHouseNum(1);
        console.log('1번집입니다.');
      } else if (house2Tile) {
        setHouseNum(2);
        console.log('2번집입니다.');
      } else if (house3Tile) {
        setHouseNum(3);
        console.log('3번집입니다.');
      } else if (house4Tile) {
        setHouseNum(4);
        console.log('4번집입니다.');
      } else if (house5Tile) {
        setHouseNum(5);
        console.log('5번집입니다.');
      } else if (house6Tile) {
        setHouseNum(6);
        console.log('6번집입니다.');
      } else if (exchangeTile) {
        setHouseNum(0);
        console.log('거래소입니다.');
      } else {
        setHouseNum(null);
        console.log('상호작용이 불가능한 위치입니다.');
      }
    }
  }

  const closModal = () => {
    setHouseNum(null);
  };

  return (
    <div>
      <div id="phaser-game-container" />
      <InteractionModal houseNum={houseNum} closeModal={closModal} />
    </div>
  );
};

export default PhaserGame;
