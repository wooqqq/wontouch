import { useParams } from 'react-router-dom';

interface RoomTitleProps {
  roomName: string | null;
  roomId: string | undefined;
}

function RoomTitle({ roomName, roomId }: RoomTitleProps) {
  const truncatedRoomId =
    roomId && roomId.length > 3 ? roomId?.slice(0, 3) : '';

  return (
    <>
      {/* 방번호-제목 */}
      <div className="flex w-[418px] h-[50px] absolute top-[10px] left-8 items-center bg-[#896A65] rounded-[10px] gap-2 px-[8px]">
        <div className="room-number">{truncatedRoomId}</div>
        <div className="font-galmuri11 text-white">{roomName}</div>
      </div>
    </>
  );
}

export default RoomTitle;
