import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import { RootState } from '../../redux/store';
import board from '../../assets/game/board.png';
import cancel from '../../assets/icon/cancel.png';
import mint_coin from '../../assets/icon/coin_mint_mini.png';
import coin from '../../assets/icon/coin_gold_mini.png';
import { updateUserStats } from '../../redux/slices/userSlice';

const GameResultModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const results = useSelector((state: RootState) => state.gameResult.results);
  const userId = useSelector((state: RootState) => state.user.id);
  const dispatch = useDispatch();
  const API_URL = import.meta.env.VITE_API_URL;

  // 닉네임을 저장하는 state
  const [nicknames, setNicknames] = useState<{ [key: string]: string }>({});

  // rank에 따라 색상을 반환하는 함수
  const getRankColor = (rank: number) => {
    switch (rank) {
      case 1:
        return "gold";  // 금색
      case 2:
        return "silver";  // 은색
      case 3:
        return "#cd7f32";  // 동색 (청동색)
      default:
        return "black";  // 기본 색상
    }
  };

  // rank에 따라 글씨 크기를 반환하는 함수
  const getRankFontSize = (rank: number) => {
    switch (rank) {
      case 1:
        return "32px";  // 1등일 때 글씨 크기
      case 2:
        return "28px";  // 2등일 때 글씨 크기
      case 3:
        return "24px";  // 3등일 때 글씨 크기
      default:
        return "20px";  // 그 외 글씨 크기
    }
  };

  // 닉네임 불러오기
  useEffect(() => {
    const fetchNicknames = async () => {
      try {
        const promises = results.map(async (player) => {
          // 각 플레이어의 정보 요청
          const response = await axios.get(`${API_URL}/user/${player.playerId}`);

          // 응답 데이터 전체를 로그로 확인
          console.log(`Response for playerId ${player.playerId}:`, response.data);

          // 응답에 있는 data 객체에서 nickname을 가져옴
          if (response.data && response.data.data && response.data.data.nickname) {
            return { playerId: player.playerId, nickname: response.data.data.nickname };
          } else {
            console.error(`Nickname not found for playerId ${player.playerId}`);
            return { playerId: player.playerId, nickname: 'Unknown' };  // 닉네임이 없을 경우 처리
          }
        });

        // 모든 API 요청이 완료될 때까지 기다림
        const nicknameResults = await Promise.all(promises);

        // 응답 데이터를 key-value 쌍으로 변환하여 상태에 저장
        const nicknameMap = nicknameResults.reduce((acc, { playerId, nickname }) => {
          acc[playerId] = nickname;
          return acc;
        }, {} as { [key: string]: string });

        setNicknames(nicknameMap);  // 닉네임 상태 업데이트
        console.log("Nickname map:", nicknameMap); // 업데이트 확인

      } catch (error) {
        console.error("닉네임을 불러오는 중 오류 발생:", error);
      }
    };

    // results 배열이 있는지 확인 후 닉네임 요청
    if (results.length > 0) {
      fetchNicknames();
    }
  }, [results]);

  useEffect(() => {
    if (!userId) return;

    const playerStat = results.find((player) => player.playerId === String(userId));

    if (playerStat) {
      dispatch(updateUserStats({
        tierPoint: playerStat.tierPoint,
        mileage: playerStat.mileage,
      }));
    }
  }, []);

  // results 배열을 rank로 정렬
  const sortedResults = [...results].sort((a, b) => a.rank - b.rank);

  return (
    <>
      <div className="fixed inset-0 bg-black bg-opacity-50 z-50" onClick={onClose}></div>

      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="relative p-8 rounded-lg shadow-lg w-[70%] h-auto bg-white z-30">
          <img src={board} alt="배경 이미지" className="absolute top-0 left-0 w-full h-full z-10 opacity-20" />
          <button onClick={onClose} className="absolute top-5 right-5 z-40">
            <img src={cancel} alt="닫기" className="w-8 h-8" />
          </button>

          <div className="relative z-20">
            <h2 className="text-4xl font-bold text-center mb-8">게임 결과</h2>
            <div className="grid grid-cols-5 gap-2 text-center font-semibold text-lg">
              <div className='text-[28px] mb-3'>순위</div>
              <div className='text-[28px]'>닉네임</div>
              <div className='text-[28px]'>총 자산</div>
              <div className='text-[28px]'>경험치</div>
              <div className='text-[28px]'>마일리지</div>
              {sortedResults.map((result) => (
                <React.Fragment key={result.playerId}>
                  <div
                    className="py-3 px-3"
                    style={{
                      color: getRankColor(result.rank),
                      fontSize: getRankFontSize(result.rank),
                    }}
                  >
                    {result.rank}등
                  </div>
                  <div className="py-3">{nicknames[result.playerId] || '불러오는 중...'}</div>
                  <div className="py-3 text-yellow-500 flex justify-center">
                    <img src={coin} className='mr-2' /> {result.totalGold.toLocaleString()}
                  </div>
                  <div className="py-3">{result.tierPoint}</div>
                  <div className="py-3 flex justify-center">
                    <img src={mint_coin} className='mr-2' />{result.mileage}
                  </div>
                </React.Fragment>
              ))}
            </div>
            <div className="mt-8 flex justify-center">
              <button
                className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600"
                onClick={onClose}
              >
                나가기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default GameResultModal;
