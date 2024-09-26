// 방장
function RoomHostInfo() {
  return (
    <div className="text-center">
      <div className="w-[157px] h-[178px] rounded-[20px] border-[#FE0] border-[5px] bg-[#FFF2D1] px-[21px] py-[10px] ">
        <div>레벨 이미지</div>
        <div>캐릭터 이미지</div>
        <div className="user-name">회원 닉네임</div>
        <div className="host-user">방장</div>
      </div>
    </div>
  );
}

// 일반 사용자
function RoomUserInfo() {
  return (
    <div className="text-center">
      <div className="w-[157px] h-[178px] rounded-[20px] border-[#FFF] border-[5px] bg-[#FFF2D1] px-[21px] py-[10px]">
        <div>레벨 이미지</div>
        <div>캐릭터 이미지</div>
        <div className="user-name">회원 닉네임</div>
        <div className="ready-user">준비</div>
      </div>
    </div>
  );
}

// 사람 없음 (초대 가능)
function RoomInvite() {
  return (
    <div className="w-[157px] h-[178px] rounded-[20px] border-[#FFF] border-[5px] bg-[#FFF2D1] px-[21px] py-[10px]">
      <div className="plus-invite">+</div>
    </div>
  );
}
export { RoomHostInfo, RoomUserInfo, RoomInvite };
