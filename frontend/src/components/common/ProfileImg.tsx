import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { characterImages } from '../../assets/avatarImages';

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
