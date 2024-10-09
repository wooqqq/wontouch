import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ProfileImg from '../common/ProfileImg';
import LevelImg from '../common/LevelImg';
import LevelText from '../common/LevelText';
import Experience from '../common/Experience';
import Modal from '../common/Modal';
import DeleteUser from './DeleteUser';

import confirm from '../../assets/icon/confirm.png';
import {
  setUserDescription,
  setUserNickname,
} from '../../redux/slices/userSlice';

export default function EditProfile() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const API_LINK = import.meta.env.VITE_API_URL;
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
  const [nickname, setNickname] = useState<string>('');
  const [isNicknameAvailable, setIsNicknameAvailable] = useState<
    boolean | null
  >(null);
  const [description, setDescription] = useState<string>('');
  const [complete, setComplete] = useState<boolean>(false);

  const goBack = () => {
    setComplete(false);
    navigate('/lobby');
  };

  const openDeleteModal = () => setDeleteUser(true);
  const closeDeleteModal = () => setDeleteUser(false);

  useEffect(() => {
    setNickname(userNickname);
    setDescription(userDescription);
  }, [userNickname, userDescription]);

  // 닉네임 중복 체크
  const handleCheckNickname = async () => {
    if (!nickname) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    try {
      const response = await axios.post(
        `${API_LINK}/user-profile/nickname/duplicate-check`,
        {
          nickname: nickname,
        },
      );

      // response.data.data가 false로 나와야 닉네임 중복을 피함.
      if (response.data.data) {
        setIsNicknameAvailable(false);
      } else {
        setIsNicknameAvailable(true);
      }
    } catch (error) {
      console.log('닉네임 중복 확인 불가', error);
    }
  };

  // 닉네임, 한 줄 소개 변경
  const patchInfo = async () => {
    if (!nickname) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    console.log(description);

    if (description.length > 15) {
      alert('한 줄 소개는 15자를 넘을 수 없습니다.');
      return;
    }

    if (isNicknameAvailable === false) {
      alert('중복된 닉네임입니다. 다른 닉네임을 입력해주세요.');
      return;
    }

    const isKorean = /^[가-힣]*$/.test(nickname); // 한글만
    const isEnglish = /[a-zA-Z]*$/.test(nickname); // 영어만
    const isValidNickname = /^[가-힣a-zA-Z]*$/.test(nickname); // 한글, 영어 혼용

    // 유효성 검사
    if (isKorean && nickname.length > 6) {
      alert('한글은 최대 6자까지 입력할 수 있습니다.');
      return;
    } else if (isEnglish && nickname.length > 10) {
      alert('영어는 최대 10자까지 입력할 수 있습니다.');
      return;
    } else if (isValidNickname && nickname.length > 10) {
      alert('한글, 영어 혼합은 최대 10자까지 입력할 수 있습니다.');
      return;
    }

    // 닉네임이 변경되었다면
    if (userNickname !== nickname) {
      try {
        await axios.patch(`${API_LINK}/user-profile/nickname/update`, {
          userId: userId,
          nickname: nickname,
        });
        dispatch(setUserNickname(nickname));
      } catch (error) {
        alert('마일리지가 부족합니다.');
        return;
      }
    }

    // 한 줄 소개가 변경되었다면
    if (userDescription !== description) {
      try {
        await axios.patch(`${API_LINK}/user-profile/description`, {
          userId: userId,
          description: description,
        });
        dispatch(setUserDescription(description));
      } catch {}
    }

    setComplete(true);
  };

  return (
    <div className="flex flex-col items-center p-10">
      <div className="mint-title mb-6">회원 정보 수정</div>
      <div className="yellow-box w-8/12 h-[550px] p-10 px-20">
        <div className="flex justify-between items-center px-10 mb-10">
          <div className="profile-img w-[190px] h-[190px] flex items-center justify-center">
            <ProfileImg characterName={userCharacterName} />
          </div>
          <div className="flex flex-col justify-center items-center">
            <div className="yellow-box p-3 px-8 w-10/12 flex justify-center flex-col bg-[#fffffe]">
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
        <div className="flex items-center">
          <div className="flex flex-col items-center justify-center w-4/12">
            <div className="text-[#10AB7D] text-3xl">닉네임</div>
            <div className="text-[#10AB7D] text-xs">
              (수정 시 10,000 마일리지 필요)
            </div>
          </div>
          <input
            className="font-['Galmuri11'] w-5/12 h-[40px] mr-6 p-3 signup-input px-6"
            id="nickname"
            placeholder="한글 6자, 영어 10자, 혼용 10자 제한"
            value={nickname}
            onChange={(event) => setNickname(event.target.value)}
          ></input>
          <div className="w-3/12">
            <button
              className="ready-button w-[130px] h-[15px] text-2xl border-[#10AB7D] border-4"
              onClick={handleCheckNickname}
            >
              중복확인
            </button>
          </div>
        </div>
        <div className="text-left ml-56 font-['Galmuri11'] min-h-[24px] mb-6">
          {isNicknameAvailable === true && (
            <p className="text-green-600">사용 가능한 닉네임입니다.</p>
          )}
          {isNicknameAvailable === false && (
            <p className="text-red-600">이미 사용 중인 닉네임입니다.</p>
          )}
        </div>
        <div className="flex items-center mb-6">
          <div className="text-[#10AB7D] text-3xl w-4/12 flex justify-center">
            한 줄 소개
          </div>
          <input
            className="font-['Galmuri11'] w-8/12 h-[40px] mr-6 p-3 signup-input px-6"
            id="description"
            placeholder="한글 기준 최대 15자 입력 가능"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          ></input>
        </div>
        <div className="flex justify-between px-24">
          <button
            className="cancel-button w-5/12 h-[50px] text-3xl"
            onClick={goBack}
          >
            취소
          </button>
          <button
            className="logout-button w-5/12 h-[50px] text-3xl"
            onClick={patchInfo}
          >
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

      {complete && (
        <Modal>
          <div className="yellow-box w-2/5 h-[200px] border-[#36EAB5] bg-[#FFFEEE] p-8">
            <div className="white-text text-4xl mb-10">
              수정이 완료되었습니다!
            </div>
            <button onClick={goBack}>
              <img src={confirm} alt="" />
            </button>
          </div>
        </Modal>
      )}
    </div>
  );
}
