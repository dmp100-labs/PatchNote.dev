import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../components/UserList/UserList.css';

export function Home() {
  const navigate = useNavigate();

  const onButtonClick = () => {
    navigate('/users');
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '100px' }}>
      <h1>PatchNote11 Web</h1>
      <p>아래 버튼을 누르면 유저 목록을 불러옵니다.</p>

      <button onClick={onButtonClick} className="load-button">
        공차민승제주완민성 화이팅
      </button>
    </div>
  );
}