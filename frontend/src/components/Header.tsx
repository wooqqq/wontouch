import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { RootState } from '../redux/store';
import { useEffect } from 'react';
import { setToken } from '../redux/slices/authSlice';

// 토큰 테스트용!
function Header() {
  const dispatch = useDispatch();
  const token = useSelector((state: RootState) => state.auth.token);

  // 컴포넌트가 마운트될 때 localStorage에서 토큰 읽기
  useEffect(() => {
    const storedToken = localStorage.getItem('access_token');
    if (storedToken) {
      // localStorage에서 읽은 토큰을 Redux 스토어에 저장
      dispatch(setToken(storedToken));
    }
  }, [dispatch]);

  return (
    <div className="mb-10">
      {/* {token ? (
        <>
          <div>로그인 상태입니다.</div>
          <div>토큰: {token}</div>
        </>
      ) : (
        <div>로그인 해주세요.</div>
      )} */}
    </div>
  );
}

export default Header;
