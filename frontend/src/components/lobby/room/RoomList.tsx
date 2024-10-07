import React, { useState } from 'react';
import axios from 'axios';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import map from '../../../assets/map/map.png';

import lock from '../../../assets/icon/lock.png';

interface RoomList {}

export default function RoomList() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const navigate = useNavigate();

  // 방 목록 호출 api
  const [roomList, setRoomList] = useState<any[]>([]);

  const getRoomList = async () => {
    const response = await axios.get(`${API_LINK}/lobby/list`);
    setRoomList(response.data.data);
    console.log(response.data.data[0]);
  };

  useEffect(() => {
    getRoomList();
  }, []);

  // 방 이동 로직
  const clickRoom = (roomId: string) => {
    navigate(`/wait/${roomId}`);
  };

  return (
    <div>
      {roomList.length > 0 ? (
        roomList
          .slice()
          .reverse()
          .map((room, index) => (
            <button>
              <div
                key={room.roomId}
                className="room-box m-3 p-2"
                onClick={() => clickRoom(room.roomId)}
              >
                <div className="room-info p-0.5 px-4 mb-2 flex justify-start">
                  <span className="text-lg font-['Galmuri11-bold'] text-yellow-300 mr-4">
                    {(index + 1).toString().padStart(3, '0')}
                  </span>
                  <span className="text-lg font-['Galmuri11'] text-white text-center">
                    {room.roomName}
                  </span>
                </div>
                <div className="room-info flex p-2 mx-auto">
                  <span className="mr-4 h-16 overflow-hidden">
                    <img src={map} alt="맵 미리보기" className="w-72" />
                  </span>
                  <div className="flex flex-col items-center justify-center">
                    {room.secret ? (
                      <div>
                        <img src={lock} alt="" className="w-6 h-8" />
                      </div>
                    ) : (
                      <div className="w-6 h-8"></div>
                    )}
                    <div>
                      <div className="white-text mt-2">
                        {room.currentPlayersCount}/8
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </button>
          ))
      ) : (
        <div className="white-title">생성된 방이 없습니다.</div>
      )}
    </div>
  );
}
