import { Link } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="h-screen w-64 bg-gray-800 text-white flex flex-col p-4">
      <h1 className="text-xl font-bold mb-6">Order Engine</h1>
        <Link to="/place-order" className="block px-4 py-2 hover:bg-gray-200">
        ➕ Place Order
        </Link>
        <Link to="/orders" className="block px-4 py-2 hover:bg-gray-200">
        📋 All Orders
        </Link>
        <Link to="/positions" className="block px-4 py-2 hover:bg-gray-200">
        📊 Position Overview
        </Link>
    </div>
  );
};

export default Sidebar;