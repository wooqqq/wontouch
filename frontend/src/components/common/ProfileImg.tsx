import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

import boy from '../../assets/background/characters/stand/boy.png';
import curlyhairBoy from '../../assets/background/characters/stand/curlyhair_boy.png';
import flowerGirl from '../../assets/background/characters/stand/flower_girl.png';
import girl from '../../assets/background/characters/stand/girl.png';
import goblin from '../../assets/background/characters/stand/goblin.png';
import kingGoblin from '../../assets/background/characters/stand/king_goblin.png';
import ninjaSkeleton from '../../assets/background/characters/stand/ninja_skeleton.png';
import skeleton from '../../assets/background/characters/stand/skeleton.png';

// 이미지 매핑 객체 생성
const characterImages: { [key: string]: string } = {
  boy: boy,
  curlyhair_boy: curlyhairBoy,
  flower_girl: flowerGirl,
  girl: girl,
  goblin: goblin,
  king_goblin: kingGoblin,
  ninja_skeleton: ninjaSkeleton,
  skeleton: skeleton,
};

// props 타입 정의
interface ProfileImgProps {
  characterName: string;
}

export default function ProfileImg({ characterName }: ProfileImgProps) {
  const userCharacterName = useSelector(
    (state: RootState) => state.user.characterName,
  );

  // userCharacterName이 string인지 확인 후 이미지 가져오기
  const profileImgSrc =
    typeof userCharacterName === 'string'
      ? characterImages[userCharacterName]
      : null;

  return (
    <div>
      {profileImgSrc ? (
        <img src={profileImgSrc} className="p-1.5" />
      ) : (
        <div>캐릭터 이미지를 찾을 수 없습니다.</div>
      )}
    </div>
  );
}
