import EditProfile from '../components/edit/EditProfile';
import EditCharacter from '../components/edit/EditCharacter';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Header from '../components/common/Header';
import BackButton from '../components/common/BackButton';

function Edit() {
  const navigate = useNavigate();

  // 뒤로 가기
  const clickArrow = () => {
    navigate('/lobby');
  };
  return (
    <>
      <div className="flex justify-between items-center">
        <BackButton />
        <Header />
      </div>
      <Routes>
        <Route path="profile" element={<EditProfile />} />
        <Route path="character" element={<EditCharacter />} />
      </Routes>
    </>
  );
}

export default Edit;
