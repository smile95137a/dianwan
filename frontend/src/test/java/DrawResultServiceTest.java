import com.one.frontend.dto.DrawRequest;
import com.one.frontend.service.DrawResultService;
import com.one.model.*;
import com.one.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = DrawResultServiceTest.class)
public class DrawResultServiceTest {

    @Mock
    private DrawRepository drawRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrizeDetailRepository prizeDetailRepository;

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private DrawResultService drawResultService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleDraw_TwoDraws() throws Exception {
        // Mock数据
        List<PrizeDetail> prizeDetails = new ArrayList<>();
        PrizeDetail prizeDetail1 = new PrizeDetail();
        prizeDetail1.setPrizeDetailId(1);
        prizeDetail1.setQuantity(10);
        prizeDetail1.setPrizeId(1L);
        prizeDetails.add(prizeDetail1);

        PrizeDetail prizeDetail2 = new PrizeDetail();
        prizeDetail2.setPrizeDetailId(2);
        prizeDetail2.setQuantity(20);
        prizeDetail2.setPrizeId(1L);
        prizeDetails.add(prizeDetail2);

        PrizeDetail prizeDetail3 = new PrizeDetail();
        prizeDetail3.setPrizeDetailId(3);
        prizeDetail3.setQuantity(20);
        prizeDetail3.setPrizeId(1L);
        prizeDetails.add(prizeDetail3);

        PrizeDetail prizeDetail4 = new PrizeDetail();
        prizeDetail4.setPrizeDetailId(4);
        prizeDetail4.setQuantity(10);
        prizeDetail4.setPrizeId(1L);
        prizeDetails.add(prizeDetail4);

        PrizeDetail prizeDetail5 = new PrizeDetail();
        prizeDetail5.setPrizeDetailId(5);
        prizeDetail5.setQuantity(20);
        prizeDetail5.setPrizeId(1L);
        prizeDetails.add(prizeDetail5);
        when(prizeDetailRepository.getAllPrizeDetails()).thenReturn(prizeDetails);

        when(userRepository.getBalance(anyInt())).thenReturn(1000);

        // Mock Prize data
        Prize prize1 = new Prize();
        prize1.setPrizeId(1);
        prize1.setRemainingQuantity(80);
        prize1.setPrice(BigDecimal.valueOf(250));
        when(prizeRepository.getPrizeById(1)).thenReturn(prize1);

        Prize prize2 = new Prize();
        prize2.setPrizeId(2);
        prize2.setRemainingQuantity(20);
        prize2.setPrice(BigDecimal.valueOf(250));
        when(prizeRepository.getPrizeById(2)).thenReturn(prize2);


        List<DrawRequest> drawRequests = new ArrayList<>();
        DrawRequest drawRequest1 = new DrawRequest();
        drawRequest1.setPrizeDetailId(1L);
        drawRequest1.setAmount(new BigDecimal("100.00"));
        drawRequests.add(drawRequest1);

        DrawRequest drawRequest2 = new DrawRequest();
        drawRequest2.setPrizeDetailId(1L);
        drawRequest2.setAmount(new BigDecimal("200.00"));
        drawRequests.add(drawRequest2);

        // 执行方法
        drawResultService.handleDraw(1, drawRequests ,1L);

        // 验证方法调用
        verify(userRepository).deductUserBalance(anyInt(), any(BigDecimal.class));
        verify(prizeDetailRepository, times(2)).updatePrizeDetailQuantity(any(PrizeDetail.class));
        verify(drawRepository).insertBatch(anyList());
        verify(orderRepository).insertOrder(any(Order.class));
        verify(orderDetailRepository, times(2)).insertOrderDetail(any(OrderDetail.class));
    }
}
