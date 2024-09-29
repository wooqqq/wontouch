import map from '../../assets/map/map.png';

function MapInfo() {
  return (
    <div className="yellow-box mt-3">
      <div className="mint-title">맵</div>
      <div>
        <img src={map} alt="맵 미리보기" />
      </div>
    </div>
  );
}

export default MapInfo;
