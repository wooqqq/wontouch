import React, { useState } from 'react';
import axios from 'axios';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';
import Modal from '../../common/Modal';
import FriendDelete from './FriendDelete';

import boy from '../../../assets/background/characters/stand/boy.png';
import curlyhairBoy from '../../../assets/background/characters/stand/curlyhair_boy.png';
import flowerGirl from '../../../assets/background/characters/stand/flower_girl.png';
import girl from '../../../assets/background/characters/stand/girl.png';
import goblin from '../../../assets/background/characters/stand/goblin.png';
import kingGoblin from '../../../assets/background/characters/stand/king_goblin.png';
import ninjaSkeleton from '../../../assets/background/characters/stand/ninja_skeleton.png';
import skeleton from '../../../assets/background/characters/stand/skeleton.png';
import cancel from '../../../assets/icon/cancel.png';
import love from '../../../assets/icon/expression_love.png';

// 이미지 매핑 객체 생성
const characterImages: { [key: string]: string } = {
  boy: boy,
  curlyhair_boy: curlyhairBoy,
  flower_girl: flowerGirl,
  girl: girl,
  goblin: goblin,
  king_goblin: kingGoblin,
  ninja_skeleton: ninjaSkeleton,
  skeleton: skeleton,
};

export default function FriendProfile({
  closeProfile,
  friendId,
}: {
  closeProfile: () => void;
  friendId: number;
}) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const userId = useSelector((state: RootState) => state.user.id);

  const [nickname, setNickname] = useState();
  const [description, setDescription] = useState();
  const [searchedCharacter, setSearchedCharacter] = useState<string>('');
  const [deleteModal, setDeleteModal] = useState<boolean>(false);

  const getFriendProfile = async () => {
    try {
      const response = await axios.get(`${API_LINK}/user/${friendId}`);
      setNickname(response.data.data.nickname);
      setDescription(response.data.data.description);
      const characterName = response.data.data.characterName;

      // 검색된 사용자의 프로필 사징 매핑
      if (characterImages[characterName]) {
        setSearchedCharacter(characterImages[characterName]);
      }
    } catch {}
  };

  // 삭제 확인 모달
  const openDeleteModal = () => {
    setDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setDeleteModal(false);
  };

  // 삭제 요청
  const deleteFriend = async () => {
    try {
      const response = await axios.delete(`${API_LINK}/friend/delete`, {
        data: {
          userId: userId,
          friendId: friendId,
        },
      });
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getFriendProfile();
  }, []);

  return (
    <div className="yellow-box w-1/2 h-[470px] p-6 px-10 border-[#36EAB5] bg-[#FFFEEE]">
      <div className="flex justify-end" onClick={closeProfile}>
        <button>
          <img src={cancel} alt="프로필 창 닫기" />
        </button>
      </div>
      <div>
        <div className="flex justify-between px-10 mt-4">
          <div className="friend-search-box w-48 h-48 rounded-full overflow-hidden">
            <img src={searchedCharacter} alt="" className="p-4" />
          </div>
          <div className="flex flex-col items-center">
            <div className="friend-search-box w-56 h-[100px] rounded-3xl flex-col mt-4">
              <div>남작</div>
              <div className="white-text text-3xl">{nickname}</div>
            </div>
            <div className="relative mt-6 w-80 h-[35px]">
              <div className="absolute exp-text z-10 w-80 h-[35px]">
                804/1084(74.169%)
              </div>
              <div className="absolute z-5 w-40 h-[35px] exp"></div>
              <div className="exp-background w-80 h-[35px]"></div>
            </div>
          </div>
        </div>
        <div className="flex justify-between items-center px-10 mt-12">
          <div className="friend-search-box white-text text-2xl w-10/12 h-[60px] rounded-3xl flex-col">
            {description}
          </div>
          <div className="flex items-center" onClick={openDeleteModal}>
            <button>
              <img src={love} alt="친구 삭제" />
            </button>
          </div>
        </div>
      </div>
      {deleteModal && (
        <Modal>
          <FriendDelete
            closeDeleteModal={closeDeleteModal}
            friendId={friendId}
          />
        </Modal>
      )}
    </div>
  );
}
