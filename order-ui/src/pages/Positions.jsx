import PositionList from "../components/PositionList";

const Positions = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6 border-b pb-2">📊 Position Overview</h1>
      <PositionList />
    </div>
  );
};

export default Positions;
