import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Orders from "./pages/Orders";
import OrderForm from "./components/OrderForm";
import Trades from "./pages/Trades";

const App = () => {
  return (
    <Router>
      <div className="flex min-h-screen">
        <Sidebar />
        <div className="flex-1 bg-gray-100 p-6">
          <Routes>
            <Route path="/" element={<Navigate to="/place-order" replace />} />
            <Route path="/place-order" element={<OrderForm />} />
            <Route path="/orders" element={<Orders />} />
            <Route path="/trades" element={<Trades />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
};

export default App;