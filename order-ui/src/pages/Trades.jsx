import { useEffect, useState } from "react";

const Trades = () => {
  const [trades, setTrades] = useState([]);
  const [loading, setLoading] = useState(true);
  const [symbolFilter, setSymbolFilter] = useState("");
  const [orderIdFilter, setOrderIdFilter] = useState("");

  useEffect(() => {
    const fetchTrades = async () => {
      try {
        const response = await fetch("http://localhost:8080/trades");
        if (!response.ok) throw new Error("Failed");
        const data = await response.json();
        setTrades(data);
      } catch {
        setTrades([]);
      } finally {
        setLoading(false);
      }
    };

    fetchTrades();
  }, []);

  const filteredTrades = trades.filter((trade) => {
    return (
      (symbolFilter === "" || trade.symbol.toLowerCase().includes(symbolFilter.toLowerCase())) &&
      (orderIdFilter === "" ||
        trade.buyOrderId?.includes(orderIdFilter) ||
        trade.sellOrderId?.includes(orderIdFilter))
    );
  });

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Trade History</h2>

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
          placeholder="Filter by Order ID"
          className="p-2 border rounded w-1/2"
          value={orderIdFilter}
          onChange={(e) => setOrderIdFilter(e.target.value)}
        />
      </div>

      <table className="min-w-full border text-sm">
        <thead>
          <tr className="bg-gray-200">
            <th className="p-2 border">ID</th>
            <th className="p-2 border">Symbol</th>
            <th className="p-2 border">Quantity</th>
            <th className="p-2 border">Price</th>
            <th className="p-2 border">Buy Order ID</th>
            <th className="p-2 border">Sell Order ID</th>
            <th className="p-2 border">Timestamp</th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr>
              <td colSpan="7" className="text-center p-4 text-gray-500">
                Loading...
              </td>
            </tr>
          ) : filteredTrades.length === 0 ? (
            <tr>
              <td colSpan="7" className="text-center p-4 text-gray-500">
                No trades found.
              </td>
            </tr>
          ) : (
            filteredTrades.map((trade, index) => (
              <tr key={index}>
                <td className="p-2 border">{trade.id}</td>
                <td className="p-2 border">{trade.symbol}</td>
                <td className="p-2 border">{trade.quantity}</td>
                <td className="p-2 border">{trade.price}</td>
                <td className="p-2 border">{trade.buyOrderId}</td>
                <td className="p-2 border">{trade.sellOrderId}</td>
                <td className="p-2 border">{new Date(trade.timestamp).toLocaleString()}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Trades;