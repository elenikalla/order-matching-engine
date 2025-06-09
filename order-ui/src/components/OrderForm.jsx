import { useState } from "react";

export default function OrderForm() {
  const [formData, setFormData] = useState({
    symbol: "",
    price: "",
    quantity: "",
    type: "BUY",
  });

  const [clientOrderId, setClientOrderId] = useState(null);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

 const handleSubmit = async (e) => {
  e.preventDefault();
  setSubmitting(true);
  setError(null);

  try {
    const response = await fetch("http://localhost:8080/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (!response.ok) throw new Error("Order submission failed");

    const data = await response.json();
    setClientOrderId(data.clientOrderId);

    setFormData({
      symbol: "",
      price: "",
      quantity: "",
      type: "BUY",
    });
  } catch (err) {
    setError(err.message);
  } finally {
    setSubmitting(false);
  }
};

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white shadow rounded">
      <h2 className="text-xl font-semibold mb-4">Place New Order</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          className="w-full border p-2 rounded"
          type="text"
          name="symbol"
          placeholder="Symbol (e.g. AAPL)"
          value={formData.symbol}
          onChange={handleChange}
          required
        />
        <input
          className="w-full border p-2 rounded"
          type="number"
          name="price"
          placeholder="Price"
          value={formData.price}
          onChange={handleChange}
          required
        />
        <input
          className="w-full border p-2 rounded"
          type="number"
          name="quantity"
          placeholder="Quantity"
          value={formData.quantity}
          onChange={handleChange}
          required
        />
        <select
          className="w-full border p-2 rounded"
          name="type"
          value={formData.type}
          onChange={handleChange}
        >
          <option value="BUY">BUY</option>
          <option value="SELL">SELL</option>
        </select>

        <button
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          type="submit"
          disabled={submitting}
        >
          {submitting ? "Submitting..." : "Submit Order"}
        </button>
        {clientOrderId && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mt-4">
            <strong className="font-bold">Success:</strong> Order submitted!
            <br />
            <span className="block text-sm">Client Order ID: <code>{clientOrderId}</code></span>
            <button
              className="absolute top-0 bottom-0 right-0 px-4 py-3"
              onClick={() => setClientOrderId(null)}
            >
              <span className="text-green-700 text-xl">&times;</span>
            </button>
          </div>
        )}
        {error && <div className="text-red-500">Error: {error}</div>}
      </form>
    </div>
  );
}
