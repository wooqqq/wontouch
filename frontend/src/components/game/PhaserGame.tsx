import Phaser from 'phaser';
import { useEffect } from 'react';

// 애니메이션 프레임에 대한 타입 정의
interface TileAnimationFrame {
  duration: number;
  tileid: number;
}

// 타일 애니메이션 정보에 대한 타입 정의
interface TileAnimationData {
  animation: TileAnimationFrame[];
}

// 전체 타일셋 데이터에 대한 타입 정의
interface TileData {
  [key: number]: TileAnimationData;
}

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
    };

    const game = new Phaser.Game(config);

    return () => {
      game.destroy(true);
    };
  }, []);

  let player: Phaser.Physics.Arcade.Sprite;
  let cursors: Phaser.Types.Input.Keyboard.CursorKeys;

  function preload(this: Phaser.Scene) {
    this.load.tilemapTiledJSON('map', '/testmap.json');
    this.load.image('tileset', '/spr_tileset_sunnysideworld_16px.png');
    this.load.image('bird_image', '/animals/spr_deco_bird_01_strip4.png');
    this.load.image('blinking_image', '/animals/spr_deco_blinking_strip12.png');
    this.load.image('chicken_image', './animals/spr_deco_chicken_01_strip4.png');
    this.load.image('cow_image', './animals/spr_deco_cow_strip4.png');
    this.load.image('duck_image', './animals/spr_deco_duck_01_strip4.png');
    this.load.image('pig_image', './animals/spr_deco_pig_01_strip4.png');
    this.load.image('sheep_image', './animals/spr_deco_sheep_01_strip4.png');
    this.load.image('collides', '/collides.png');
    this.load.spritesheet('player', './ninja_skeleton.png', {
      frameWidth: 16,
      frameHeight: 19,
    });
  }

  function create(this: Phaser.Scene) {
    const map = this.make.tilemap({ key: 'map' });
    const tileset = map.addTilesetImage(
      'spr_tileset_sunnysideworld_16px',
      'tileset',
      16,
      16,
    );
    const birdTileset = map.addTilesetImage('bird_fast', 'bird_image', 16, 16);
    const collidesTileset = map.addTilesetImage('collides', 'collides', 16, 16);

    if (!tileset || !birdTileset || !collidesTileset) {
      console.error('불러올 수 없음');
      return;
    }

    //레이어 선언 부분
    const backgroundLayer = map.createLayer('Background', tileset);
    const groundLayer = map.createLayer('ground', tileset);
    const seaLayer = map.createLayer('seasea', tileset);
    const shadow_seaLayer = map.createLayer('Shadow_sea', tileset);
    const river_lakeLayer = map.createLayer('river, lake', tileset);
    const dropfruitsLayer = map.createLayer('dropfruits', tileset);
    const fencesLayer = map.createLayer('fences', tileset);
    const plantLayer = map.createLayer('wheat, animal, tree, line', tileset);
    const othersssLayer = map.createLayer('othersss', tileset);
    const animals_bottomLayer = map.createLayer('animals_bottom', birdTileset);
    const vegetableLayer = map.createLayer('fruits, vegetables', tileset);
    const shadow_bottomLayer = map.createLayer('Shadow_bottom', tileset);
    const house_bottom2Layer = map.createLayer('house_bottom2', tileset);
    const house_bottomLayer = map.createLayer('house_bottom', tileset);
    const house_topLayer = map.createLayer('house_top', tileset);
    const shadow_topLayer = map.createLayer('shadow_top', tileset);
    const animals_topLayer = map.createLayer('animals_top', tileset);
    const collidesLayer = map.createLayer('collides/notpass', collidesTileset);
    collidesLayer?.setCollisionByProperty({ collides: true });

    const tileAnimations = map.tilesets[0].tileData as TileData;
    const birdAnimations = birdTileset.tileData[0].animation;
    console.log(birdAnimations);

    if(birdAnimations && birdAnimations.length > 0){
      const birdFrames = birdAnimations.map((frame) => ({
        key: 'bird_image',
        frame: frame.tileid,
        duration: frame.duration,
      }));

      this.anims.create({
        key: 'bird_fly',
        frames: birdFrames,
        frameRate: 10,
        repeat: -1,
      })
      console.log("나 움직이는중임");
    }

    Object.keys(tileAnimations).forEach((tileIdKey) => {
      const tileId = parseInt(tileIdKey, 10);

      if (tileAnimations[tileId] && tileAnimations[tileId].animation) {
        const animationFrames = tileAnimations[tileId].animation.map(
          (frame) => frame.tileid,
        );
        let frameIndex = 0;

        this.time.addEvent({
          delay: tileAnimations[tileId].animation[0].duration,
          callback: () => {
            [seaLayer, groundLayer].forEach((layer) => {
              layer?.replaceByIndex(
                animationFrames[frameIndex],
                animationFrames[(frameIndex + 1) % animationFrames.length],
              );
            });
            frameIndex = (frameIndex + 1) % animationFrames.length;
          },
          loop: true,
        });
      }
    });

    //레이어 층 결정하는 부분
    backgroundLayer?.setDepth(-3);
    seaLayer?.setDepth(-2);
    shadow_seaLayer?.setDepth(-2);
    groundLayer?.setDepth(-1);
    river_lakeLayer?.setDepth(0);
    dropfruitsLayer?.setDepth(0);
    fencesLayer?.setDepth(0);
    plantLayer?.setDepth(0);
    othersssLayer?.setDepth(0);
    animals_bottomLayer?.setDepth(0);
    vegetableLayer?.setDepth(0);
    shadow_bottomLayer?.setDepth(0);
    house_bottom2Layer?.setDepth(1);
    house_bottomLayer?.setDepth(1);
    house_topLayer?.setDepth(1);
    shadow_topLayer?.setDepth(1);
    animals_topLayer?.setDepth(1);
    collidesLayer?.setDepth(-1);

    player = this.physics.add.sprite(2240, 1380, 'player');
    player.setCollideWorldBounds(true);
    player.setScale(1.5);

    //this.physics.add.collider(player, collidesLayer as Phaser.Tilemaps.TilemapLayer);

    this.physics.world.setBounds(0, 0, 4480, 2560);
    this.cameras.main.setBounds(0, 0, 4480, 2560);
    this.cameras.main.startFollow(player, true, 0.1, 0.1);

    this.anims.create({
      key: 'walk',
      frames: this.anims.generateFrameNumbers('player', { start: 0, end: 7 }),
      frameRate: 10,
      repeat: -1,
    });

    cursors = this.input.keyboard!.createCursorKeys();
  }

  function update(this: Phaser.Scene) {
    player.setVelocity(0);

    let moving = false;
    const speed = 350;
    let vx = 0;
    let vy = 0;

    if (cursors.left.isDown) {
      vx = -speed;
      player.flipX = true;
      moving = true;
    }
    if (cursors.right.isDown) {
      vx = speed;
      player.flipX = false;
      moving = true;
    }
    if (cursors.up.isDown) {
      vy = -speed;
      moving = true;
    }
    if (cursors.down.isDown) {
      vy = speed;
      moving = true;
    }

    if (vx !== 0 && vy !== 0) {
      vx *= Math.SQRT1_2;
      vy *= Math.SQRT1_2;
    }

    player.setVelocity(vx, vy);

    if (moving) {
      if (!player.anims.isPlaying || player.anims.currentAnim?.key !== 'walk') {
        player.anims.play('walk', true);
      }
    } else {
      player.anims.stop();
      player.setFrame(0);
    }
  }

  return <div id="phaser-game-container" />;
};

export default PhaserGame;
