import { useEffect, useState } from "react";

const PositionList = () => {
  const [positions, setPositions] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/positions")
      .then((res) => res.json())
      .then(setPositions)
      .catch(console.error);
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Position Overview</h1>
      <table className="table-auto w-full border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="px-4 py-2">Symbol</th>
            <th className="px-4 py-2">Buy Qty</th>
            <th className="px-4 py-2">Sell Qty</th>
            <th className="px-4 py-2">Net</th>
            <th className="px-4 py-2">Avg Buy</th>
            <th className="px-4 py-2">Avg Sell</th>
            <th className="px-4 py-2">PnL</th>
          </tr>
        </thead>
        <tbody>
          {positions.map((pos) => (
            <tr key={pos.symbol}>
              <td className="border px-4 py-2">{pos.symbol}</td>
              <td className="border px-4 py-2">{pos.buyQty}</td>
              <td className="border px-4 py-2">{pos.sellQty}</td>
              <td className="border px-4 py-2">{pos.netPosition}</td>
              <td className="border px-4 py-2">{pos.avgBuyPrice.toFixed(2)}</td>
              <td className="border px-4 py-2">{pos.avgSellPrice.toFixed(2)}</td>
              <td className="border px-4 py-2">{pos.pnl.toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PositionList;