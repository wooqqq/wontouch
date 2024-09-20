import Phaser from 'phaser';
import  { useEffect } from 'react';

const PhaserGame = () => {
    useEffect(() => {
        const config: Phaser.Types.Core.GameConfig = {
            type: Phaser.AUTO,
            width: 800,
            height: 600,
            physics: {
                default: 'arcade',
                arcade: {
                    gravity: { x: 0, y: 0 },
                    fps: 60, // 물리 엔진의 프레임 속도 설정
                    tileBias: 16,
                    debug: true,
                }
            },
            scene: {
                preload,
                create,
                update
            }
        };

        // Phaser 게임 생성
        const game = new Phaser.Game(config);

        // 컴포넌트 언마운트 시 Phaser 게임 종료
        return () => {
            game.destroy(true);
        };
    }, []);

    let player: Phaser.Physics.Arcade.Sprite;
    let cursors: Phaser.Types.Input.Keyboard.CursorKeys;

    function preload(this: Phaser.Scene) {
        // 'player'라는 키로 스프라이트 시트를 로드함
        this.load.image('bg', './sky.png');
        this.load.spritesheet('player', './player.png', { frameWidth: 16, frameHeight: 19});
    }

    function create(this: Phaser.Scene) {
        this.physics.world.setBounds(0, 0, 1600, 1200);
        
        const bg = this.add.image(400, 300, 'bg');
        bg.setDisplaySize(2600, 2000);

        // 'player' 스프라이트 시트를 사용해 캐릭터를 생성
        player = this.physics.add.sprite(400, 300, 'player');
        player.setCollideWorldBounds(true);

        this.cameras.main.setBounds(0, 0, 1600, 1200);
        this.cameras.main.startFollow(player, true, 0.1, 0.1); // 부드러운 카메라 이동

        // 'player' 키를 사용하여 애니메이션 생성
        this.anims.create({
            key: 'walk',
            frames: this.anims.generateFrameNumbers('player', { start: 0, end: 7 }), // 'character'가 아닌 'player'로 변경
            frameRate: 10,
            repeat: -1
        });

        cursors = this.input.keyboard!.createCursorKeys();
    }

    function update(this: Phaser.Scene) {
        player.setVelocity(0);

        let moving = false;
        const speed = 160;

        if (cursors.left.isDown) {
            player.setVelocityX(-speed);
            player.flipX = true;
            moving = true;
        } else if (cursors.right.isDown) {
            player.setVelocityX(speed);
            player.flipX = false;
            moving = true;
        } else if (cursors.up.isDown) {
            player.setVelocityY(-speed);
            moving = true;
        } else if (cursors.down.isDown) {
            player.setVelocityY(speed);
            moving = true;
        }

        if (moving) {
            if (!player.anims.isPlaying || player.anims.currentAnim?.key !== 'walk') {
                player.anims.play('walk', true);
            }
        } else {
            player.anims.stop();
        }
    }

    return <div id="phaser-game-container" />;
};

export default PhaserGame;
