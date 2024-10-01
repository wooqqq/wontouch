import level05 from '../../assets/level/05_level_silver.png';
import boy from '../../assets/background/characters/stand/boy.png';

interface UserInfoProps {
  isHost: boolean; // 방장 여부
  playerId: string | undefined; // 사용자 (없으면 undefined)
  nickname: string | undefined;
  isReady: boolean; // 준비 여부
  character: string | undefined;
}

function RoomUserInfo({
  isHost,
  playerId,
  isReady,
  character,
  nickname,
}: UserInfoProps) {
  const characterImages: { [key: string]: string } = {
    boy: boy,
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
          <div>
            <img
              src={level05}
              alt="레벨"
              className="w-7 absolute right-4 top-0"
            />
          </div>
          <div className="w-full">
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
