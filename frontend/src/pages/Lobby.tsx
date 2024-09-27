import { RootState } from '../redux/store';
import { useSelector } from 'react-redux';
import { useState } from 'react';
import axios from 'axios';
import MakeRoom from '../components/lobby/MakeRoom';
import Modal from '../components/common/Modal';
import Nickname from '../components/common/Nickname';
import ProfileImg from '../components/common/ProfileImg';
import Mail from '../components/common/Mail';
import Setting from '../components/common/Setting';

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
      <div>
        <ProfileImg />
        <Nickname />
        <Mail />
        <Setting />
      </div>
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
