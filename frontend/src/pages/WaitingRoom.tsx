import Modal from "../components/common/Modal";
import FriendInvite from "../components/waitingRoom/FriendInvite";
import MapInfo from "../components/waitingRoom/MapInfo";
import ReadyButton from "../components/waitingRoom/ReadyButton";
import RoomChat from "../components/waitingRoom/RoomChat";
import RoomHowTo from "../components/waitingRoom/RoomHowTo";
import RoomTitle from "../components/waitingRoom/RoomTitle";
import RoomUserList from "../components/waitingRoom/RoomUserList";

function WaitingRoom() {
  return (
    <div className="flex">
      {/* 왼쪽 섹션 (방제목/게임 참여자 리스트/채팅) */}
      <section className="w-2/3">
        <RoomTitle />
        {/* 게임 참여 대기자 리스트 */}
        <RoomUserList />
        <RoomChat />
      </section>

      {/* 오른쪽 섹션 (맵/게임방법/준비버튼) */}
      <section className="w-1/3">
        <MapInfo />
        <RoomHowTo />
        <ReadyButton />
      </section>
      <Modal>
        <FriendInvite />
      </Modal>
    </div>
  );
}

export default WaitingRoom;
