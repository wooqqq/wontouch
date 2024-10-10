import axios from 'axios';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useDispatch } from 'react-redux';
import { useEffect } from 'react';
import ProfileImg from '../common/ProfileImg';
import Nickname from '../common/Nickname';
import Mileage from './Mileage';
import Mail from '../common/Mail';
import Setting from '../lobby/Setting';
import Modal from './Modal';
import { increaseNotificationCount } from '../../redux/slices/notificationSlice';

import mail from '../../assets/icon/mail.png';
import setting from '../../assets/icon/setting.png';

export default function Header() {
  const dispatch = useDispatch();

  const API_LINK = import.meta.env.VITE_API_URL;

  const [showMail, setShowMail] = useState<boolean>(false);
  const [showSetting, setShowSetting] = useState<boolean>(false);

  const accessToken = sessionStorage.getItem('access_token');
  const userId = useSelector((state: RootState) => state.user.id);

  const userCharacterName = useSelector(
    (state: RootState) => state.user.characterName,
  );
  const notificationCount = useSelector(
    (state: RootState) => state.notification.count,
  );

  // SSE 연결
  const setupSSE = async (userId: number) => {
    try {
      const eventSource = new EventSource(
        `${API_LINK}/notification/subscribe/${userId}`,
      );

      // 친구 신청 알림 수신 시 api 호출
      eventSource.addEventListener('addFriendRequest', (event) => {
        dispatch(increaseNotificationCount());
      });

      // 게임 초대 알림 수신 시 api 호출
      eventSource.addEventListener('addGameInvite', (event) => {
        dispatch(increaseNotificationCount());
      });
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (userId) {
      setupSSE(userId);
    }
  }, [userId]);

  // 메일함 모달
  const openMail = () => setShowMail(true);
  const closeMail = () => setShowMail(false);

  // 환경설정 모달
  const openSetting = () => setShowSetting(true);
  const closeSetting = () => setShowSetting(false);

  return (
    <div className="flex items-center space-x-3 p-3 justify-end">
      <div className="brown-box w-12 h-12">
        <ProfileImg characterName={userCharacterName} />
      </div>
      <Nickname />
      <Mileage />
      <button onClick={openMail} className="brown-box w-12 h-12 p-1">
        <img src={mail} alt="" />
        {notificationCount > 0 && (
          <span className="absolute top-3 right-32 bg-red-500 text-white rounded-full w-4 h-4 flex items-center justify-center text-xs">
            {notificationCount}
          </span>
        )}
      </button>
      <button onClick={openSetting} className="brown-box w-12 h-12 p-1">
        <img src={setting} alt="" />
      </button>

      {showMail && (
        <Modal>
          <Mail closeMail={closeMail} />
        </Modal>
      )}

      {showSetting && (
        <Modal>
          <Setting closeSetting={closeSetting} />
        </Modal>
      )}
    </div>
  );
}
