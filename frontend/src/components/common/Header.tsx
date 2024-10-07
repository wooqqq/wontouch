import { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import ProfileImg from '../common/ProfileImg';
import Nickname from '../common/Nickname';
import Mail from '../common/Mail';
import Setting from '../lobby/Setting';
import Modal from './Modal';

import mail from '../../assets/icon/mail.png';
import setting from '../../assets/icon/setting.png';

export default function Header({
  notificationCount,
}: {
  notificationCount: number;
}) {
  const [showMail, setShowMail] = useState<boolean>(false);
  const [showSetting, setShowSetting] = useState<boolean>(false);

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

  const userCharacterName = useSelector(
    (state: RootState) => state.user.characterName,
  );

  return (
    <div className="flex items-center space-x-3 p-3 justify-end">
      <div className="brown-box w-12 h-12">
        <ProfileImg characterName={userCharacterName} />
      </div>
      <Nickname />
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
