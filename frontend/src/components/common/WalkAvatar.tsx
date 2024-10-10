import boyWalk from '../../assets/background/characters/walk/boy.gif';
import curlyhairBoyWalk from '../../assets/background/characters/walk/curlyhair_boy.gif';
import girlWalk from '../../assets/background/characters/walk/girl.gif';
import flowerGirlWalk from '../../assets/background/characters/walk/flower_girl.gif';
import { useSelector } from 'react-redux';

// 이미지 매핑 객체 생성
const characterImages: { [key: string]: string } = {
  boy: boyWalk,
  curlyhair_boy: curlyhairBoyWalk,
  flower_girl: flowerGirlWalk,
  girl: girlWalk,
  // goblin: goblin,
  // king_goblin: kingGoblin,
  // ninja_skeleton: ninjaSkeleton,
  // skeleton: skeleton,
};

interface WalkAvatarProps {
  characterName: string;
}

function WalkAvatar({ characterName }: WalkAvatarProps) {
  const characterName = useSelector();
  return (
    <>
      <div></div>
    </>
  );
}

export default WalkAvatar;
