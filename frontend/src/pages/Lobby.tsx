import { RootState } from "../redux/store";
import { useSelector } from "react-redux";
import { useState } from "react";
import axios from "axios";
import MakeRoom from "../components/Lobby/MakeRoom";
import Modal from "../components/common/Modal";

const API_LINK = import.meta.env.VITE_API_URL;

function Lobby() {
  const nickname = useSelector((state: RootState) => state.user.nickname);
  const [showMakeRoom, setShowMakeRoom] = useState<boolean>(false);
  const [showFindRoom, setShowFindRoom] = useState<boolean>(false);

  const openMakeRoom = () => {
    setShowMakeRoom(true);
  };

  const closeMakeRoom = () => {
    setShowMakeRoom(false);
  };

  const openFindRoom = () => {
    setShowFindRoom(true);
  };

  const closeFindRoom = () => {
    setShowFindRoom(false);
  };

  return (
    <div>
      <div>로비입니당</div>
      <div>{nickname}님 반갑습니다!</div>
      <div>
        <button onClick={openMakeRoom}>방 만들기</button>
      </div>
      <div>
        <button onClick={openFindRoom}>방 찾기</button>
      </div>

      {showMakeRoom && (
        <Modal>
          <MakeRoom closeMakeRoom={closeMakeRoom} />
        </Modal>
      )}
    </div>
  );
}

export default Lobby;
