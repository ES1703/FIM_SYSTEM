#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pandas as pd 


# In[ ]:


def getProcessedData(FileName : str):
    #讀檔並刪除不需要的特徵
    Data = pd.read_csv(FileName,encoding ='ansi')
    Data.drop(["CityCode","AreaCode","CityCode1","CityCode2","AreaCode1","RoofMaterial","ApprDate",'UseArea','UseType','DebtPerc'],axis=1,inplace = True)
    Data["BuildComDate"] = Data["BuildComDate"].str[0:4]
    Data.reset_index(drop = True , inplace = True)
    column_name = ["LandValue","LandHoldArea","BuildPurpose","BuildMaterial","BuildType","BuildLayerArea","BuildComDate","AdjBuildArea",
                   "AreaItem","CityItem","AplraiseValue"]
    returnData = pd.DataFrame(columns=column_name)
   
    #填補BuildType空缺值    
    ind=Data[Data['BuildType'].isnull()].index
    for i in ind:
        Data.loc[i,'BuildType']='R2'
        
    #填補AdjBuildArea空缺值 
    Data["AdjBuildArea"]=Data["AdjBuildArea"].fillna(0)
    
    #將重複的案子合併
    index = 0
    while (index < (len(Data)-1)):
        drop = False
        Area = Data["AreaItem"][index]
        City = Data["CityItem"][index]
        buildMat = Data["BuildMaterial"][index]
        date = Data["BuildComDate"][index]
        value = Data["AplraiseValue"][index]
        if(Data["LoanKey"][index] == Data["LoanKey"][index+1]):
            count = 1
            startIndex=index
            while(Data["LoanKey"][index] == Data["LoanKey"][index+1] and (index+1<len(Data))):
                count = count+1
                index = index+1    
            for i in range(startIndex,index+1):
                    content=Data["BuildLayer"][i]
                    if(content.find('B')>-1 or content.find('地下')>-1):
                            drop = True 
                            break
            if(drop == False):
                LandValue = 0
                BuildingList = []           
                LandList = []
                buildpurpose = Data["BuildPurpose"][startIndex:index].mode()[0]
                buildtype = Data["BuildType"][startIndex:index].mode()[0]
                for i in range(startIndex,index+1):
                        if(LandList.count([Data["LandArea"][i],Data["LandHoldArea"][i]]) == 0):
                                LandList.append([Data["LandArea"][i],Data["LandHoldArea"][i]])
                                LandValue=LandValue+Data["LandValue"][i]
                        if(BuildingList.count([Data["BuildLayer"][i],Data["BuildLayerArea"][i],Data["AdjBuildArea"][i]]) == 0):
                                BuildingList.append([Data["BuildLayer"][i],Data["BuildLayerArea"][i],Data["AdjBuildArea"][i]])
                landholdarea = 0
                BuildLayerArea = 0
                AdjBuildArea = 0
                for i in range(len(LandList)):
                    landholdarea = landholdarea + LandList[i][1]
                for i in range(len(BuildingList)):
                    BuildLayerArea = BuildLayerArea + BuildingList[i][1]
                    AdjBuildArea = AdjBuildArea + BuildingList[i][2]
                LandValue = LandValue / len(LandList)   
                returnData=returnData.append({'LandValue':LandValue,'LandHoldArea' :landholdarea,'BuildPurpose':buildpurpose,
                          'BuildMaterial':buildMat ,'BuildType':buildtype,'BuildLayerArea':BuildLayerArea,'BuildComDate':date,
                          'AdjBuildArea':AdjBuildArea,'AreaItem':Area,'CityItem':City,'AplraiseValue':value},ignore_index=True)
        else:
            landholdarea = Data["LandHoldArea"][index]
            BuildLayerArea = Data["BuildLayerArea"][index]
            AdjBuildArea = Data["AdjBuildArea"][index]
            buildpurpose = Data["BuildPurpose"][index]
            buildtype = Data["BuildType"][index]
            LandValue = Data["LandValue"][index]
            returnData=returnData.append({'LandValue':LandValue,'LandHoldArea' :landholdarea,'BuildPurpose':buildpurpose,
                          'BuildMaterial':buildMat ,'BuildType':buildtype,'BuildLayerArea':BuildLayerArea,'BuildComDate':date,
                          'AdjBuildArea':AdjBuildArea,'AreaItem':Area,'CityItem':City,'AplraiseValue':value},ignore_index=True)
        index=index+1
        if(index == len(Data)-1):
            Area = Data["AreaItem"][index]
            City = Data["CityItem"][index]
            buildMat = Data["BuildMaterial"][index]
            date = Data["BuildComDate"][index]
            value = Data["AplraiseValue"][index]
            landholdarea = Data["LandHoldArea"][index]
            BuildLayerArea = Data["BuildLayerArea"][index]
            AdjBuildArea = Data["AdjBuildArea"][index]
            buildpurpose = Data["BuildPurpose"][index]
            buildtype = Data["BuildType"][index]
            LandValue = Data["LandValue"][index]
            returnData=returnData.append({'LandValue':LandValue,'LandHoldArea' :landholdarea,'BuildPurpose':buildpurpose,
                                'BuildMaterial':buildMat ,'BuildType':buildtype,'BuildLayerArea':BuildLayerArea,'BuildComDate':date,
                                'AdjBuildArea':AdjBuildArea,'AreaItem':Area,'CityItem':City,'AplraiseValue':value},ignore_index=True)
    
   

    #進行AreaRank的排名
    returnData["AreaRank"] = returnData["CityItem"].map({"屏東縣":1,"台東縣":1,"嘉義縣":1,"嘉義市":1,"南投縣":1,"連江縣":1,
                                           "雲林縣":2,"澎湖縣":2,"彰化縣":2,"苗栗縣":2,
                                           "台南市":3,"基隆市":3,"花蓮縣":3,"高雄市":3,"宜蘭縣":3,"桃園市":3,"台中市":3,"新竹縣":3,"新竹市":3,"金門縣":3,"澎湖縣":3,
                                           "新北市":4,
                                           "台北市":6})
    newTaipei = {"永和區","板橋區", "中和區","三重區","新店區","蘆洲區","新莊區","土城區"}  
    Taipei = {"大安區","信義區", "中正區","松山區","中山區"}

    for index in returnData.index:
        if(returnData["CityItem"][index] == "台北市"):
            if(returnData["AreaItem"][index] in Taipei):
                  returnData.loc[index,"AreaRank"]= returnData["AreaRank"][index]+1
        elif(returnData["CityItem"][index] == "新北市"):
            if(returnData["AreaItem"][index] in newTaipei):
                  returnData.loc[index,"AreaRank"]= returnData["AreaRank"][index]+1
    
    #新增TotalArea、LandValue兩個特徵
    returnData['TotalArea']=returnData["BuildLayerArea"] + returnData["AdjBuildArea"]+returnData['LandHoldArea']
    returnData = returnData[["LandValue","LandHoldArea","BuildPurpose","BuildMaterial","BuildType","BuildLayerArea","BuildComDate","AdjBuildArea",
                   "AreaItem","CityItem","AreaRank","TotalArea","AplraiseValue"]]
    returnData['LandValue'] = returnData['LandValue'].astype(int)
    return returnData

