async function test() {
  try {
    const userId = '550e8400-e29b-41d4-a716-446655440000';
    const productId = '550e8400-e29b-41d4-a716-446655440001';
    
    // Add item
    let res = await fetch(`http://localhost:8084/carts/${userId}/items`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId, quantity: 1 })
    });
    console.log("Add status:", res.status);
    let cart = await res.json();
    console.log("Cart after add:", JSON.stringify(cart, null, 2));
    
    // Delete item
    res = await fetch(`http://localhost:8084/carts/${userId}/items/${productId}`, {
      method: 'DELETE'
    });
    console.log("Delete status:", res.status);
    console.log("Delete body:", await res.text());
    
  } catch (err) {
    console.error(err);
  }
}
test();
