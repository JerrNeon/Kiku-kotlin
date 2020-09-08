// -------------------------------------------------------------------------------------
// CMB Confidential
//
// Copyright (C) 2015年8月3日 China Merchants Bank Co., Ltd. All rights reserved.
//
// No part of this file may be reproduced or transmitted in any form or by any
// means,
// electronic, mechanical, photocopying, recording, or otherwise, without prior
// written permission of China Merchants Bank Co., Ltd.
//
// -------------------------------------------------------------------------------------
package com.jn.kiku.ttp.pay.cmblife

/**
 * 掌上生活回调
 */
interface ICmblifeListener {
    /**
     * 捕获掌上生活回调
     *
     * @param requestCode 请求码，与唤起掌上生活时传入的参数一致
     * @param resultMap 返回参数
     */
    fun onCmblifeCallBack(requestCode: String?, resultMap: Map<String?, String?>?)
}