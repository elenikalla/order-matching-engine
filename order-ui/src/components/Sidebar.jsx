import { Link } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="h-screen w-64 bg-gray-800 text-white flex flex-col p-4">
      <h1 className="text-xl font-bold mb-6">Order Engine</h1>
      <Link to="/" className="mb-2 hover:bg-gray-700 p-2 rounded">Place Order</Link>
      <Link to="/orders" className="hover:bg-gray-700 p-2 rounded">View Orders</Link>
    </div>
  );
};

export default Sidebar;