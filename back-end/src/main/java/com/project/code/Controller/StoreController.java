@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // 🔴 MOVE THIS UP (VERY IMPORTANT)
    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        Store store = storeRepository.findByid(storeId);
        return store != null;
    }

    // 🔴 MOVE THIS UP (VERY IMPORTANT)
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.saveOrder(request);
            response.put("message", "Order placed successfully");
        } catch (Exception e) {
            response.put("Error", e.getMessage());
        }
        return response;
    }
