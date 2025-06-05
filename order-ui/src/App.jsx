import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Home from "./pages/Home";
import Orders from "./pages/Orders";
import Positions from "./pages/Positions";
import OrderForm from "./components/OrderForm";

const App = () => {
  return (
    <Router>
      <div className="flex min-h-screen">
        {/* Sidebar on the left */}
        <Sidebar />

        {/* Main content area on the right */}
        <div className="flex-1 bg-gray-100 p-6">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/place-order" element={<OrderForm />} />
            <Route path="/orders" element={<Orders />} />
            <Route path="/positions" element={<Positions />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
};

export default App;