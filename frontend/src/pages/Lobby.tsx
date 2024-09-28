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

import mail from '../assets/icon/mail.png';
import setting from '../assets/icon/setting.png';

function Lobby() {
  const [showMakeRoom, setShowMakeRoom] = useState<boolean>(false);
  const [showFindRoom, setShowFindRoom] = useState<boolean>(false);
  const [showMail, setShowMail] = useState<boolean>(false);
  const [showSetting, setShowSetting] = useState<boolean>(false);

  // 방 생성 모달
  const openMakeRoom = () => {
    setShowMakeRoom(true);
  };

  const closeMakeRoom = () => {
    setShowMakeRoom(false);
  };

  // 방 찾기 모달
  const openFindRoom = () => {
    setShowFindRoom(true);
  };

  const closeFindRoom = () => {
    setShowFindRoom(false);
  };

  // 메일함 모달
  const openMail = () => {
    setShowMail(true);
  };

  const closeMail = () => {
    setShowMail(false);
  };

  // 환경설정 모달
  const openSetting = () => {
    setShowSetting(true);
  };

  const closeSetting = () => {
    setShowSetting(false);
  };

  return (
    <div>
      <div>
        <ProfileImg />
        <Nickname />
        <div onClick={openMail}>
          <img src={mail} alt="" />
        </div>
        <div onClick={openSetting}>
          <img src={setting} alt="" />
        </div>
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

      {showMail && (
        <Modal>
          <Mail />
        </Modal>
      )}

      {showSetting && (
        <Modal>
          <Setting />
        </Modal>
      )}
    </div>
  );
}

export default Lobby;
