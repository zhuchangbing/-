<script setup>
import userHeader from '@/components/user/userHeader.vue';
import userFooter from '@/components/user/userFooter.vue';
import { ref } from 'vue';


</script>

<template>
	<div class="common-layout">
		<el-container>
			<el-header>
				<userHeader
				v-model:skuList="skuList"></userHeader>
			</el-header>

			<div class="bg">
				<el-main>
					<div class="center">
						<RouterView v-model:skuList="skuList"/>
					</div>
				</el-main>
				<el-footer>
					<userFooter></userFooter>
				</el-footer>
			</div>

		</el-container>
	</div>
</template>



<style scoped>
.el-header, .el-main, .el-footer{
	padding: 0px;
	height: auto;
}
.el-footer {
	padding-top: 20px;
}
.center {
	width: 1250px;
	height: auto;
	margin: 0 auto;
}
.bg{
	width: 100%;
	height: auto;
	background-color: rgb(241, 243, 245);

}
</style>