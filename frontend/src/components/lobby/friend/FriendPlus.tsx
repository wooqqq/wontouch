import React from 'react';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';
import { useState } from 'react';

import boy from '../../../assets/background/characters/stand/boy.png';
import curlyhairBoy from '../../../assets/background/characters/stand/curlyhair_boy.png';
import flowerGirl from '../../../assets/background/characters/stand/flower_girl.png';
import girl from '../../../assets/background/characters/stand/girl.png';
import goblin from '../../../assets/background/characters/stand/goblin.png';
import kingGoblin from '../../../assets/background/characters/stand/king_goblin.png';
import ninjaSkeleton from '../../../assets/background/characters/stand/ninja_skeleton.png';
import skeleton from '../../../assets/background/characters/stand/skeleton.png';
import search from '../../../assets/icon/search.png';
import cancel from '../../../assets/icon/cancel.png';
import confirm from '../../../assets/icon/confirm.png';

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

export default function FriendPlus({
  closeFriend,
}: {
  closeFriend: () => void;
}) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const [id, setId] = useState<number>();
  const [nickname, setNickname] = useState<string>('');
  const [searchedNickname, setSearchedNickname] = useState<string>('');
  const [searchedCharacter, setSearchedCharacter] = useState<string>('');
  const [state, setState] = useState<'init' | 'success' | 'fail' | 'friend'>(
    'init',
  );

  const userId = useSelector((state: RootState) => state.user.id);

  const handleSearch = async () => {
    try {
      const response = await axios.post(`${API_LINK}/user/search`, {
        userId: userId,
        nickname: nickname,
      });
      const characterName = response.data.data.characterName;
      setSearchedNickname(response.data.data.nickname);

      // 검색된 사용자의 프로필 사징 매핑
      if (characterImages[characterName]) {
        setSearchedCharacter(characterImages[characterName]);
      }

      const isFriend = response.data.data.friend;
      const friendId = response.data.data.friendId;
      console.log(response.data.data.friendId);

      // 성공 시 상태 업데이트
      if (isFriend === false) {
        setState('success');
        setId(friendId);
      } else {
        setState('friend');
      }
    } catch (error) {
      // 실패 시 상태 업데이트
      console.log(error);
      setState('fail');
    }
  };

  const handleConfirm = async () => {
    console.log(userId);
    console.log(id);
    try {
      const response = await axios.post(`${API_LINK}/friend/request`, {
        fromUserId: userId,
        toUserId: id,
      });
    } catch {}
  };

  return (
    <div className="yellow-box w-1/2 h-[470px] p-6 px-10 border-[#36EAB5] bg-[#FFFEEE]">
      <div className="mint-title text-5xl mb-6">친구 추가</div>
      <div className="relative mb-6">
        <input
          type="text"
          placeholder="닉네임"
          className="input-tag font-['Galmuri11'] w-full h-[80px] p-10 text-2xl mb-8"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              handleSearch(); // 엔터가 눌리면 handleSearch 호출
            }
          }}
        />
        <button className="absolute right-4 top-1/2 transform -translate-y-1/2">
          <img src={search} alt="친구 검색" onClick={handleSearch} />
        </button>
      </div>

      {state === 'init' && (
        <div className="h-[160px] mb-4">
          <div className="flex items-center justify-center h-full">
            <div className="white-text text-4xl">어떤 친구를 찾으시나요?</div>
          </div>
        </div>
      )}
      {state === 'success' && (
        <div className="h-[160px] mb-4">
          <div className="flex justify-between px-36 mb-6">
            <div className="friend-search-box w-24 h-24 rounded-full overflow-hidden">
              <img src={searchedCharacter} alt="" className="p-4" />
            </div>
            <div className="friend-search-box w-56 rounded-3xl flex-col">
              <div>남작</div>
              <div className="white-text text-3xl">{searchedNickname}</div>
            </div>
          </div>
          <div className="mb-4">
            <div className="can-search-text">친구 요청이 가능합니다.</div>
          </div>
        </div>
      )}
      {state === 'fail' && (
        <div className="h-[160px] mb-4">
          <div className="flex justify-between px-36 mb-6">
            <div className="friend-search-box w-24 h-24 rounded-full overflow-hidden"></div>
            <div className="friend-search-box w-56 rounded-3xl flex-col"></div>
          </div>
          <div className="mb-4">
            <div className="cant-search-text">닉네임을 다시 확인해주세요.</div>
          </div>
        </div>
      )}
      {state === 'friend' && (
        <div className="h-[160px] mb-4">
          <div className="flex justify-between px-36 mb-6">
            <div className="friend-search-box w-24 h-24 rounded-full overflow-hidden">
              <img src={searchedCharacter} alt="" className="p-4" />
            </div>
            <div className="friend-search-box w-56 rounded-3xl flex-col">
              <div>남작</div>
              <div className="white-text text-3xl">{searchedNickname}</div>
            </div>
          </div>
          <div className="mb-4">
            <div className="can-search-text">이미 친구인 사용자입니다.</div>
          </div>
        </div>
      )}
      <div className="flex justify-between px-60">
        <button>
          <img src={cancel} alt="모달 닫기" onClick={closeFriend} />
        </button>
        <button>
          <img src={confirm} alt="친구 추가" onClick={handleConfirm} />
        </button>
      </div>
    </div>
  );
}
