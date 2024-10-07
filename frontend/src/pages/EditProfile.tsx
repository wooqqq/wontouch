import { useSelector } from 'react-redux';
import { RootState } from '../redux/store';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ProfileImg from '../components/common/ProfileImg';
import LevelImg from '../components/common/LevelImg';
import LevelText from '../components/common/LevelText';
import Experience from '../components/common/Experience';
import Modal from '../components/common/Modal';
import DeleteUser from '../components/setting/DeleteUser';

import pencil from '../assets/icon/pencil.png';

export default function EditProfile() {
  const navigate = useNavigate();
  const userId = useSelector((state: RootState) => state.user.id);
  const userCharacterName = useSelector(
    (state: RootState) => state.user.characterName,
  );
  const userNickname = useSelector((state: RootState) => state.user.nickname);
  const tierPoint = useSelector((state: RootState) => state.user.tierPoint);
  const userDescription = useSelector(
    (state: RootState) => state.user.description,
  );
  const [deleteUser, setDeleteUser] = useState<boolean>(false);

  const goBack = () => {
    navigate('/lobby');
  };

  const openDeleteModal = () => setDeleteUser(true);
  const closeDeleteModal = () => setDeleteUser(false);

  return (
    <div className="flex flex-col items-center p-10">
      <div className="mint-title mb-6">회원 정보 수정</div>
      <div className="yellow-box w-8/12 h-[550px] p-10 px-20">
        <div className="flex justify-between items-center px-10 mb-10">
          <div className="profile-img w-[190px] h-[190px] flex items-center justify-center">
            <ProfileImg characterName={userCharacterName} />
          </div>
          <div className="flex flex-col justify-center items-center">
            <div className="yellow-box p-3 px-8">
              <div className="flex items-center justify-center mb-2">
                <div className="mr-2 text-xl level-text">
                  <LevelText tierPoint={Number(tierPoint)} />
                </div>
                <div className="w-6">
                  <LevelImg tierPoint={Number(tierPoint)} />
                </div>
              </div>
              <div className="white-text text-3xl">{userNickname}</div>
            </div>
            <div>
              <Experience tierPoint={Number(tierPoint)} />
            </div>
          </div>
        </div>
        <div className="flex items-center justify-between mb-10">
          <div className="flex flex-col items-center justify-center w-[175px]">
            <div className="text-[#10AB7D] text-3xl">닉네임</div>
            <div className="text-[#10AB7D] text-xs">
              (수정 시 10000 마일리지 필요)
            </div>
          </div>
          <div className="font-['Galmuri11'] w-[350px] h-[40px] mr-6 p-3 signup-input flex items-center">
            {userNickname}
          </div>
          <button>
            <img src={pencil} alt="" className="w-8" />
          </button>
        </div>
        <div className="flex items-center justify-between mb-10">
          <div className="text-[#10AB7D] text-3xl w-[175px] flex justify-center">
            한 줄 소개
          </div>
          <div className="font-['Galmuri11'] w-[350px] h-[40px] mr-6 p-3 signup-input flex items-center">
            {userDescription}
          </div>
          <button>
            <img src={pencil} alt="" className="w-8" />
          </button>
        </div>
        <div className="flex justify-between">
          <button
            className="cancel-button w-5/12 h-[50px] text-3xl"
            onClick={goBack}
          >
            취소
          </button>
          <button className="logout-button w-5/12 h-[50px] text-3xl">
            저장
          </button>
        </div>
        <div className="flex justify-center mt-3 font-['Galmuri11'] text-[#888888]">
          <button onClick={openDeleteModal}>회원 탈퇴</button>
        </div>
      </div>

      {deleteUser && (
        <Modal>
          <DeleteUser closeDeleteModal={closeDeleteModal} />
        </Modal>
      )}
    </div>
  );
}
