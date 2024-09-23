export class Player {
  sprite: Phaser.Physics.Arcade.Sprite;

  constructor(scene: Phaser.Scene, x: number, y: number){
    this.sprite =scene.physics.add.sprite(x, y, 'player');
    this.sprite.setCollideWorldBounds(true);
    this.sprite.setFrame(0);
  }

  handleMovement(cursors: Phaser.Types.Input.Keyboard.CursorKeys){
    if(cursors.left.isDown){
      this.sprite.setVelocityX(-15);
      if(this.sprite.anims.currentAnim?.key !== 'left'){
        this.sprite.anims.play('run', true);
      }
    }else if( cursors.right.isDown) {
      this.sprite.setVelocityX(15);
      if(this.sprite.anims.currentAnim?.key !== 'right'){
        this.sprite.anims.play('run', true);
      }
    }
    else if( cursors.up.isDown){
      this.sprite.setVelocityY(-15);
      if(this.sprite.anims.currentAnim?.key !== 'up'){
        this.sprite.anims.play('run', true);
      }
    }
    else if(cursors.down.isDown){
      this.sprite.setVelocityY(15);
      if(this.sprite.anims.currentAnim?.key !== 'down'){
        this.sprite.anims.play('run', true);
      }
    }
    else {
      this.sprite.setVelocity(0);
      this.sprite.anims.stop();
      this.sprite.setFrame(0);
    }
  }
}