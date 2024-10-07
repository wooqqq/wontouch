import boy from '../../assets/background/characters/stand/boy.png';
import girl from '../../assets/background/characters/stand/girl.png';
import curlyhairBoy from '../../assets/background/characters/stand/curlyhair_boy.png';
import flowerGirl from '../../assets/background/characters/stand/flower_girl.png';
import goblin from '../../assets/background/characters/stand/goblin.png';
import kingGoblin from '../../assets/background/characters/stand/king_goblin.png';
import ninjaSkeleton from '../../assets/background/characters/stand/ninja_skeleton.png';
import skeleton from '../../assets/background/characters/stand/skeleton.png';

import LevelImg from '../common/LevelImg';

interface UserInfoProps {
  isHost: boolean; // 방장 여부
  playerId: string | undefined; // 사용자 (없으면 undefined)
  nickname: string | undefined;
  isReady: boolean; // 준비 여부
  character: string | undefined;
  tierPoint: string | undefined;
}

// 대기방 접속자 정보
function RoomUserInfo({
  isHost,
  playerId,
  isReady,
  character,
  nickname,
  tierPoint,
}: UserInfoProps) {
  const characterImages: { [key: string]: string } = {
    boy: boy,
    girl: girl,
    curlyhair_boy: curlyhairBoy,
    flower_girl: flowerGirl,
    goblin: goblin,
    king_goblin: kingGoblin,
    ninja_skeleton: ninjaSkeleton,
    skeleton: skeleton,
  };

  if (!playerId) {
    // 유저가 없을 경우 (친구 초대 버튼)
    return (
      <div className="w-[157px] h-[178px] rounded-[20px] border-[#FFF] border-[5px] bg-[#FFF2D1] px-[21px] py-[10px] flex items-center justify-center">
        <div className="plus-invite">+</div>
      </div>
    );
  } else {
    // 사용자가 있는 경우
    return (
      <div className="text-center">
        <div
          className={`w-[157px] h-[178px] rounded-[20px] border-[5px] bg-[#FFF2D1] px-[21px] py-[10px] relative
            ${isHost ? 'border-[#FE0]' : isReady ? 'border-[#36eab5]' : 'border-[#FFF] '}`}
        >
          <div className="w-7 absolute right-4 top-0">
            <LevelImg tierPoint={tierPoint ? Number(tierPoint) : 0} />
          </div>
          <div className="w-[92%] mx-auto my-0">
            <img
              src={character ? characterImages[character] : ''}
              alt="캐릭터"
            />
          </div>
          <div className="user-name">{nickname}</div>
          {isHost ? (
            <div className="host-user absolute bottom-3 left-11">방장</div>
          ) : isReady ? (
            <div className="ready-user absolute bottom-3 left-3">준비 완료</div>
          ) : null}
        </div>
      </div>
    );
  }
}
export { RoomUserInfo };
