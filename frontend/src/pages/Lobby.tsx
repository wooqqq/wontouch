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
import search from '../assets/icon/search.png';
import lock from '../assets/icon/lock.png';
import unlock from '../assets/icon/unlock.png';

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
      <div className="flex items-center space-x-4 p-3 justify-end">
        <div className="brown-box w-12 h-12">
          <ProfileImg />
        </div>
        <div className="brown-box w-44 h-12">
          <Nickname />
        </div>
        <div onClick={openMail} className="brown-box w-12 h-12 p-1">
          <img src={mail} alt="" />
        </div>
        <div onClick={openSetting} className="brown-box w-12 h-12 p-1">
          <img src={setting} alt="" />
        </div>
      </div>

      <div className="justify-between">
        <div className="yellow-box w-8/12 h-5/6 flex flex-col justify-center p-2">
          <div className="flex space-x-4 mb-4">
            <div>
              <button
                onClick={openMakeRoom}
                className="ready-button w-52 h-14 text-3xl"
              >
                방 만들기
                <img src={search} alt="" />
              </button>
            </div>
            <div>
              <button
                onClick={openFindRoom}
                className="ready-button w-52 h-14 text-3xl"
              >
                방 찾기
                <img src={search} alt="" />
              </button>
            </div>
          </div>
          <div className="list-box overflow-auto flex flex-wrap justify-between p-2 h-full">
            <div className="room-box w-96 h-36 m-3 p-2">
              <div className="room-info p-0.5 px-2 mb-2">
                <span className="text-lg font-['Galmuri11-bold'] text-yellow-300 mr-2.5">
                  001
                </span>
                <span className="text-lg font-['Galmuri11'] text-white text-center">
                  방 제목
                </span>
              </div>
              <div className="room-info flex p-2">
                <span className="mr-4">
                  <img src="src/assets/tmp.png" alt="" className="w-72 h-16" />
                </span>
                <div className="flex flex-col items-center justify-center">
                  <div>
                    <img src={lock} alt="" className="w-6 h-8" />
                  </div>
                  <div>
                    <div className="white-text">3/8</div>
                  </div>
                </div>
              </div>
            </div>
            {/* 삭제할 부분 */}
            <div className="room-box w-96 h-36 m-3 p-2">
              <div className="room-info p-0.5 px-2 mb-2">
                <span className="text-lg font-['Galmuri11-bold'] text-yellow-300 mr-2.5">
                  001
                </span>
                <span className="text-lg font-['Galmuri11'] text-white text-center">
                  방 제목
                </span>
              </div>
              <div className="room-info flex p-2">
                <span className="mr-4">
                  <img src="src/assets/tmp.png" alt="" className="w-72 h-16" />
                </span>
                <div className="flex flex-col items-center justify-center">
                  <div>
                    <img src={lock} alt="" className="w-6 h-8" />
                  </div>
                  <div>
                    <div className="white-text">3/8</div>
                  </div>
                </div>
              </div>
            </div>

            <div className="room-box w-96 h-36 m-3 p-2">
              <div className="room-info p-0.5 px-2 mb-2">
                <span className="text-lg font-['Galmuri11-bold'] text-yellow-300 mr-2.5">
                  001
                </span>
                <span className="text-lg font-['Galmuri11'] text-white text-center">
                  방 제목
                </span>
              </div>
              <div className="room-info flex p-2">
                <span className="mr-4">
                  <img src="src/assets/tmp.png" alt="" className="w-72 h-16" />
                </span>
                <div className="flex flex-col items-center justify-center">
                  <div>
                    <img src={lock} alt="" className="w-6 h-8" />
                  </div>
                  <div>
                    <div className="white-text">3/8</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
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
