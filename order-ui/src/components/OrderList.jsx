import { useEffect, useState } from "react";

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [symbolFilter, setSymbolFilter] = useState("");
  const [clientOrderIdFilter, setClientOrderIdFilter] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/orders")
      .then((res) => res.json())
      .then((data) => setOrders(data))
      .catch((err) => console.error("Error fetching orders:", err));
  }, []);

  const filteredOrders = orders.filter((order) => {
    return (
      (symbolFilter === "" || order.symbol.toLowerCase().includes(symbolFilter.toLowerCase())) &&
      (clientOrderIdFilter === "" || order.clientOrderId?.includes(clientOrderIdFilter))
    );
  });

  return (
    <div>
      <div className="mb-4 flex gap-4">
        <input
          type="text"
          placeholder="Filter by Symbol"
          className="p-2 border rounded w-1/3"
          value={symbolFilter}
          onChange={(e) => setSymbolFilter(e.target.value)}
        />
        <input
          type="text"
          placeholder="Filter by Client Order ID"
          className="p-2 border rounded w-1/2"
          value={clientOrderIdFilter}
          onChange={(e) => setClientOrderIdFilter(e.target.value)}
        />
      </div>

      <table className="min-w-full border text-sm">
        <thead>
          <tr className="bg-gray-200">
            <th className="p-2 border">Order ID</th>
            <th className="p-2 border">Client Order ID</th>
            <th className="p-2 border">Symbol</th>
            <th className="p-2 border">Type</th>
            <th className="p-2 border">Price</th>
            <th className="p-2 border">Quantity</th>
            <th className="p-2 border">Status</th>
          </tr>
        </thead>
        <tbody>
          {filteredOrders.map((order) => (
            <tr key={order.id}>
              <td className="p-2 border">{order.id}</td>
              <td className="p-2 border">{order.clientOrderId}</td>
              <td className="p-2 border">{order.symbol}</td>
              <td className="p-2 border">{order.type}</td>
              <td className="p-2 border">{order.price}</td>
              <td className="p-2 border">{order.quantity}</td>
              <td className="p-2 border">{order.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default OrderList;