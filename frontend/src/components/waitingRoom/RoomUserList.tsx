import { RoomHostInfo, RoomUserInfo, RoomInvite } from "./RoomUserInfo";

function RoomUserList() {
  return (
    <div className="waitingroom-brown-box flex flex-wrap justify-center gap-9">
      <div>
        <RoomHostInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <div>
        <RoomUserInfo />
      </div>
      <RoomInvite />
    </div>
  );
}
export default RoomUserList;
