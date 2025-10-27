package kr.co.hkcloud.palette3.config.aspect;


/**
 * AOP를 이용한 방식 고려중...현재는 개별 @Transaction 어노테이션을 사용함 - 20210626
 * 
 * @author leeiy
 *
 */
//@Configuration
public class TransactionAspect
{
//    private static final String AOP_TRANSACTION_METHOD_NAME = "*";
//    private static final String AOP_TRANSACTION_EXPRESSION  = "execution(* kr.co.hkcloud.palette..app.*ServiceImpl.*(..))";
//
//    @Autowired
//    private TransactionManager transactionManager;
//
//
//    @Bean
//    public TransactionInterceptor transactionAdvice()
//    {
////        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
////        transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);
////        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
////        transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
////
////        MatchAlwaysTransactionAttributeSource txAttributeSource = new MatchAlwaysTransactionAttributeSource();
////        txAttributeSource.setTransactionAttribute(transactionAttribute);
//
//        RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();
//        txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//        txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();
//        txMethods.put(AOP_TRANSACTION_METHOD_NAME, txAttribute);
//
//        NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
//        txAttributeSource.setNameMap(txMethods);
//
//        return new TransactionInterceptor(transactionManager, txAttributeSource);
//    }
//
//
//    @Bean
//    public Advisor transactionAdvisor()
//    {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression(AOP_TRANSACTION_EXPRESSION);
//
//        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
//    }
}
