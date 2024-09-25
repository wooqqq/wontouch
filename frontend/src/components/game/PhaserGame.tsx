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

    if (!tileset) {
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
    const animals_bottomLayer = map.createLayer('animals_bottom', tileset);
    const vegetableLayer = map.createLayer('fruits, vegetables', tileset);

    const tileAnimations = map.tilesets[0].tileData as TileData;

    // 모든 애니메이션이 있는 타일을 순회하며 설정
    Object.keys(tileAnimations).forEach((tileIdKey) => {
      const tileId = parseInt(tileIdKey, 10);

      // 해당 타일의 애니메이션 프레임 정보 가져오기
      if (tileAnimations[tileId] && tileAnimations[tileId].animation) {
        const animationFrames = tileAnimations[tileId].animation.map(
          (frame) => frame.tileid,
        );
        let frameIndex = 0;

        // 타일의 애니메이션 순서에 맞춰 프레임을 교체
        this.time.addEvent({
          delay: tileAnimations[tileId].animation[0].duration, // 프레임 지속 시간 사용
          callback: () => {
            // 애니메이션 프레임 교체
            seaLayer?.replaceByIndex(
              animationFrames[frameIndex],
              animationFrames[(frameIndex + 1) % animationFrames.length],
            );
            
            frameIndex = (frameIndex + 1) % animationFrames.length; // 프레임 순환
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

    player = this.physics.add.sprite(2240, 1280, 'player');
    player.setCollideWorldBounds(true);
    player.setScale(2);

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
    const speed = 350;  // 기본 속도
    let vx = 0;
    let vy = 0;

    // 캐릭터 이동 구현
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

    // 대각선 이동 시 속도 조정
    if (vx !== 0 && vy !== 0) {
      // 대각선 이동이므로 속도를 조정 (피타고라스 정리 활용)
      vx *= Math.SQRT1_2;  // 1 / sqrt(2)
      vy *= Math.SQRT1_2;
    }

    // 속도 적용
    player.setVelocity(vx, vy);

    // 애니메이션 처리
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
