import { jwtDecode } from 'jwt-decode';
import { setUserId } from '../../../redux/slices/userSlice';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

interface DecodedToken {
  userId: number;
}

function GoogleLogin() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleGoogleLogin = () => {
    // 본인 구글 토큰 입력
    const accessToken = 'token';

    localStorage.setItem('access_token', accessToken);
    const decodedToken = jwtDecode<DecodedToken>(accessToken);
    dispatch(setUserId(decodedToken.userId));

    console.log('구글 로그인 성공, Access Token:', accessToken);
    console.log('userId: ', decodedToken.userId);

    navigate('/lobby');
  };

  return (
    <div>
      <img
        src="src/assets/Login/google_login.png"
        alt="구글 로그인 이미지"
        onClick={handleGoogleLogin}
      />
    </div>
  );
}

export default GoogleLogin;
