import { RootState } from '../redux/store';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import axios from 'axios';
import Modal from '../components/common/Modal';
import MakeRoom from '../components/lobby/MakeRoom';
import FindRoom from '../components/lobby/FindRoom';
import ProfileImg from '../components/common/ProfileImg';
import Nickname from '../components/common/Nickname';
import Mail from '../components/common/Mail';
import Setting from '../components/common/Setting';

function Lobby() {
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

      {showFindRoom && (
        <Modal>
          <FindRoom closeFindRoom={closeFindRoom} />
        </Modal>
      )}
    </div>
  );
}

export default Lobby;
