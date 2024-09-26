import { useParams } from "react-router-dom";

function RoomTitle() {
  const { roomId } = useParams();
  return (
    <>
      {/* 방번호-제목 */}
      <div className="flex w-[418px] h-[50px] items-center bg-[#896A65] rounded-[10px] gap-2 px-[8px]">
        <div className="room-number">{roomId}</div>
        <div className="font-galmuri11 text-white">빨리 게임해요</div>
      </div>
    </>
  );
}

export default RoomTitle;
