import Phaser from 'phaser';

export const createPlayerMovement = (
  _scene: Phaser.Scene,
  player: Phaser.Physics.Arcade.Sprite,
  cursors: Phaser.Types.Input.Keyboard.CursorKeys
) => {
  const speed = 350;
  let vx = 0;
  let vy = 0;

  player.setVelocity(0);
  let moving = false;

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

};
