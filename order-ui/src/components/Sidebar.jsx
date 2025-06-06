import { Link } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="h-screen w-64 bg-gray-800 text-white flex flex-col p-4">
      <h1 className="text-xl font-bold mb-6">Order Matching Engine</h1>
        <Link to="/place-order" className="block px-4 py-2 rounded hover:bg-gray-700 hover:text-white">
        ➕ Place Order
        </Link>
        <Link to="/orders" className="block px-4 py-2 rounded hover:bg-gray-700 hover:text-white">
        📋 All Orders
        </Link>
        <Link to="/trades" className="block px-4 py-2 rounded hover:bg-gray-700 hover:text-white">
        🔄 Trades
        </Link>
    </div>
  );
};

export default Sidebar;