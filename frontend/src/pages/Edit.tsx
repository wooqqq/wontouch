import EditProfile from '../components/edit/EditProfile';
import EditCharacter from '../components/edit/EditCharacter';
import { Route, Routes } from 'react-router-dom';
import Header from '../components/common/Header';

function Edit() {
  return (
    <>
      <Header notificationCount={1} />
      <Routes>
        <Route path="profile" element={<EditProfile />} />
        <Route path="character" element={<EditCharacter />} />
      </Routes>
    </>
  );
}

export default Edit;
